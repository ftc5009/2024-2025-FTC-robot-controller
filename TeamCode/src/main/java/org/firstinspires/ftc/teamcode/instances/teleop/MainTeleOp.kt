package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.components.Arm
import org.firstinspires.ftc.teamcode.components.Arm.Companion.gear_target
import org.firstinspires.ftc.teamcode.components.Arm.Companion.slide_target

@TeleOp(name = "Main")
class MainTeleOp: LinearOpMode() {
	override fun runOpMode() {
		val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
		motors.setPowerRatio(1.0)
		val arm = Arm(this)
		arm.init_motors()
		while(opModeInInit()) {
			arm.go_to_target()
			telemetry.addData("arm error: ", arm.pid_slide.update(slide_target.get() * arm.slide_inches_ticks - arm.slide.getPosition()))
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

			if(gamepad1.x){
				gear_target.set(45.0)
				slide_target.set(5.0)
			}
			if(gamepad1.y){
				gear_target.set(39.0)
				slide_target.set(23.5)
			}

			telemetry.update()
		}
	}
}
