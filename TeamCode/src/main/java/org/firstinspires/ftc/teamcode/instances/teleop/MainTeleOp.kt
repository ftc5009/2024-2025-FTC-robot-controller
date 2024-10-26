package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.components.Arm

@TeleOp(name = "Main")
class MainTeleOp: LinearOpMode() {
	override fun runOpMode() {
		val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
		motors.setPowerRatio(1.0)
		val arm = Arm(this)
		while(opModeInInit()) {
			telemetry.addData("arm error: ", arm.pid_slide.update(arm.slide_target.get() * arm.slide_inches_ticks - arm.slide.getPosition()))
			telemetry.update()
		}
		waitForStart()
		while (opModeIsActive()) {
			val drive = -gamepad1.left_stick_y.toDouble()
			val strafe = gamepad1.left_stick_x.toDouble()
			val rotate = gamepad1.right_stick_x.toDouble()
			motors.gamepadMove(drive, strafe, rotate)
			//arm.go_to_target()

			if(gamepad1.x){
				arm.gear_target.set(10.0)
				arm.slide_target.set(5.0)
			}

			telemetry.update()
		}
	}
}
