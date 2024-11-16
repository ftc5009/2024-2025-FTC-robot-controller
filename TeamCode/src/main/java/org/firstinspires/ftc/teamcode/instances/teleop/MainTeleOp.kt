package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
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
		arm.init_teleOp()
		var y_count = 0
		var x_count = 0
		var y_pressed = false
		var x_pressed = false
		var free_slide = false
		var free_wrist = false
		while(opModeInInit()) {
			arm.go_to_target()
			telemetry.addData("arm error: ", arm.pid_slide.update(slide_target.get() - arm.slide.getPosition() / arm.slide_inches_ticks))
			telemetry.addData("slide pos", arm.slide.getPosition())
			telemetry.addData("gear pos", arm.gear.getPosition() / arm.gear_degrees_ticks)
			telemetry.update()
		}
		waitForStart()
		while (opModeIsActive()) {
			val drive = -gamepad1.left_stick_y.toDouble()
			val strafe = gamepad1.left_stick_x.toDouble()
			val rotate = gamepad1.right_stick_x.toDouble()
			motors.gamepadMove(drive, strafe, rotate)
			arm.go_to_target()

			if(gamepad1.y && !y_pressed){
				if(y_count == 0){
					//high basket
					gear_target.set(42.0)
					slide_target.set(27.0)
					arm.wrist_servos(0.75, 0.75)
				}else if(y_count == 1){
					//low basket
					gear_target.set(37.0)
					slide_target.set(5.0)
					arm.wrist_servos(0.5, 0.5)
				}
				y_count += 1
				y_pressed = true
				x_count = 0
			} else if(!gamepad1.y && y_pressed){
				y_pressed = false
			}
			if(gamepad1.a){
				//sample pick up from submersible
				gear_target.set(135.0)
				slide_target.set(5.0)
				free_slide = true
				free_wrist = true
			}
			if(gamepad1.x && x_pressed){
				//high chamber for specimens
				if(x_count == 0){
					//high chamber
					gear_target.set(70.0)
					slide_target.set(20.5)
					arm.wrist_servos(0.5, 0.5)
				}else if(x_count == 1){
					//low chamber
					gear_target.set(95.0)
					slide_target.set(10.0)
					arm.wrist_servos(0.5, 0.5)
				}
				x_count += 1
				x_pressed = false
				y_count = 0
				free_slide = true
				free_wrist = false
			}else if(!gamepad1.x && x_pressed){
				x_pressed = false
			}
			if(gamepad1.b){
				//wall pick up
				gear_target.set(10.0)
				slide_target.set(1.0)
			}
			if(gamepad1.right_trigger > 0.5 && free_slide){
				slide_target.set(Range.clip(slide_target.get() + 0.1, 0.0, 27.0))
			}else if(gamepad1.left_trigger > 0.5 && free_slide){
				slide_target.set(Range.clip(slide_target.get() - 0.1, 0.0, 27.0))
			}
			if (gamepad1.dpad_up && free_wrist){
				if(arm.right_wrist.position != 1.0 && arm.left_wrist.position != 1.0 && arm.right_wrist.position + arm.left_wrist.position < 2.0) {
					arm.right_wrist.position += 0.01
					arm.left_wrist.position += 0.01
				}
			} else if(gamepad1.dpad_down && free_wrist) {
				if(arm.right_wrist.position != 0.0 && arm.left_wrist.position != 0.0 && arm.right_wrist.position + arm.left_wrist.position >1.0){
					arm.right_wrist.position -= 0.01
					arm.left_wrist.position -= 0.01
				}
			}
			if (gamepad1.dpad_left && free_wrist) {
				if(arm.right_wrist.position != 1.0 && arm.left_wrist.position != 0.0) {
					arm.right_wrist.position += 0.01
					arm.left_wrist.position -= 0.01
				}
			} else if (gamepad1.dpad_right && free_wrist) {
				if(arm.right_wrist.position != 0.0 && arm.left_wrist.position != 1.0) {
					arm.right_wrist.position -= 0.01
					arm.left_wrist.position += 0.01
				}
			}
			/*
			if(gamepad1.right_bumper){
				arm.intake_servos(1.0)
			} else if (gamepad1.left_bumper){
				arm.intake_servos(-1.0)
			} else {
				arm.intake_servos(0.0)
			}
			*/
			telemetry.update()
		}
	}
}
