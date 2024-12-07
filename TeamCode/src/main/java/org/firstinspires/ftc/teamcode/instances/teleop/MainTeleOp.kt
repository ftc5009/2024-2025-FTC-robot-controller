package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.components.Arm
import org.firstinspires.ftc.teamcode.components.Arm.Companion.gear_target
import org.firstinspires.ftc.teamcode.components.Arm.Companion.slide_target

@TeleOp(name = "Main")
class MainTeleOp: LinearOpMode() {
	override fun runOpMode() {
		val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
		motors.setPowerRatio(1.0)
		val arm = Arm(this)

		var y_count = 0.0
		var x_count = false
		var a_count = false
		var a_button = false
		var y_pressed = false
		var x_pressed = false
		var a_pressed = false
		var free_slide = false
		var free_wrist = false
		var free_gear = false
		while (opModeInInit()) {
			telemetry.addData(
				"arm error: ",
				arm.pid_slide.update(slide_target.get() - arm.slide.getPosition() / arm.slide_inches_ticks)
			)
			telemetry.addData("slide pos", arm.slide.getPosition())
			telemetry.addData("gear pos", arm.gear.getPosition() / arm.gear_degrees_ticks)
			telemetry.update()
		}
		waitForStart()
		arm.init_teleOp()
		while (opModeIsActive()) {
			val drive = -gamepad1.left_stick_y.toDouble()
			val strafe = gamepad1.left_stick_x.toDouble()
			val rotate = gamepad1.right_stick_x.toDouble()
			motors.gamepadMove(drive, strafe, rotate)
			if (gamepad1.a || y_count > 0.0) {
				arm.gear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT)
				arm.gear.setPower(0.0)
				a_button = true
				arm.pid_gear.reset()
			} else {
				if (a_button) {
					arm.gear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
					a_button = false
				}
				arm.go_to_target()
			}
			if (gamepad2.y && !y_pressed) {
				if (y_count == 2.0) {
					//high basket
					gear_target.set(27.0)
					slide_target.set(27.0)
					arm.wrist_servos(0.75, 0.75)
				} else if (y_count == 1.0) {
					//low basket
					gear_target.set(27.0)
					slide_target.set(12.0)
					arm.wrist_servos(0.75, 0.75)
				} else if (y_count == 0.0) {
					//no slide
					gear_target.set(27.0)
					slide_target.set(3.0)
				}
				y_count = (y_count + 1.0) % 3.0
				y_pressed = true
				x_count = false
				free_gear = true
				free_wrist = true
				free_slide = true
				a_count = false
			} else if (!gamepad2.y && y_pressed) {
				y_pressed = false
			}
			if (gamepad1.b) {
				gear_target.set(113.0)
				slide_target.set(2.0)
				free_slide = true
				free_wrist = true
				y_count = 0.0
				a_count = false
				x_count = false
			}
			if (gamepad2.a && !gamepad2.start && !a_pressed && slide_target.get() < 10.0) {
				//sample pick up from submersible
				if (!a_count) {
					gear_target.set(97.0)
					slide_target.set(5.0)
				} else {
					gear_target.set(115.0)
				}
				a_count = !a_count
				free_slide = true
				free_wrist = true
				free_gear = true
				y_count = 0.0
				a_pressed = true
			} else if (!gamepad2.a && a_pressed) {
				a_pressed = false
			}

			if (gamepad2.x && !x_pressed) {
				//high chamber for specimens
				if (!x_count) {
					//high chamber
					gear_target.set(73.0)
					slide_target.set(13.5)
					arm.wrist_servos(0.5, 0.5)
				} else {
					//low chamber
					gear_target.set(95.0)
					slide_target.set(5.0)
					arm.wrist_servos(0.5, 0.5)
				}
				x_count = !x_count
				x_pressed = true
				y_count = 0.0
				free_slide = true
				free_wrist = false
				free_gear = true
				a_count = false
			} else if (!gamepad2.x && x_pressed) {
				x_pressed = false
			}
			if (gear_target.get() < 45.0) {
				if (gamepad1.right_trigger > 0.5 && free_slide) {
					slide_target.set(Range.clip(slide_target.get() + 0.1, 0.0, 27.0))
				} else if (gamepad1.left_trigger > 0.5 && free_slide) {
					slide_target.set(Range.clip(slide_target.get() - 0.1, 0.0, 27.0))
				} else {
					if (gamepad1.right_trigger > 0.5 && free_slide) {
						slide_target.set(Range.clip(slide_target.get() + 0.1, 0.0, 20.0))
					} else if (gamepad1.left_trigger > 0.5 && free_slide) {
						slide_target.set(Range.clip(slide_target.get() - 0.1, 0.0, 20.0))
					}
				}
			}
			if (gamepad1.right_bumper && free_gear) {
				gear_target.set(Range.clip(gear_target.get() + 0.1, 20.0, 125.0))
			} else if (gamepad1.left_bumper && free_gear) {
				gear_target.set(Range.clip(gear_target.get() - 0.1, 20.0, 125.0))
			}
			if (gamepad2.dpad_up && free_wrist) {
				if (arm.right_wrist.position != 1.0 && arm.left_wrist.position != 1.0 && arm.right_wrist.position + arm.left_wrist.position < 2.0) {
					arm.right_wrist.position += 0.01
					arm.left_wrist.position += 0.01

				} else if (gamepad2.dpad_down && free_wrist) {
					if (arm.right_wrist.position != 0.0 && arm.left_wrist.position != 0.0 && arm.right_wrist.position + arm.left_wrist.position > 1.0) {
						arm.right_wrist.position -= 0.01
						arm.left_wrist.position -= 0.01
					}
				}
			}
			if (gamepad2.dpad_left && free_wrist) {
				if(arm.right_wrist.position != 1.0 && arm.left_wrist.position != 0.0) {
					arm.right_wrist.position += 0.01
					arm.left_wrist.position -= 0.01
				}
			} else if (gamepad2.dpad_right && free_wrist) {
				if(arm.right_wrist.position != 0.0 && arm.left_wrist.position != 1.0) {
					arm.right_wrist.position -= 0.01
					arm.left_wrist.position += 0.01
				}
			}
			if (gamepad2.right_bumper) {
				arm.intake_servos(1.0)//outake
			} else if (gamepad2.left_bumper) {
				arm.intake_servos(-1.0)
			} else {
				arm.intake_servos(0.0)
			}
			telemetry.addData("gear", gear_target.get())
			telemetry.addData("R", arm.right_wrist.position)
			telemetry.addData("L", arm.left_wrist.position)
			telemetry.update()
		}
	}
}

