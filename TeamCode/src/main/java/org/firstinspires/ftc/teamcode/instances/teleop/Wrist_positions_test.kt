package org.firstinspires.ftc.teamcode.instances.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.components.Arm

@TeleOp(name = "Wrist test")
class Wrist_positions_test: LinearOpMode() {
	override fun runOpMode() {
		val arm = Arm(this)
		val stopper = hardwareMap.get(Servo::class.java, "stopper")
		var left = 0.4
		var right = 0.4
		val half_way = 180.0
		waitForStart()
		while (opModeIsActive()) {
			arm.wrist_servos(left, right)
			if (gamepad1.dpad_up){
				if(right != 1.0 && left != 1.0 && right + left < 1.0) {
					right += 0.01
					left += 0.01
				}
			} else if(gamepad1.dpad_down) {
				if(right != 0.0 && left != 0.0 && right + left >0.0){
					right -= 0.01
					left -= 0.01
				}
			}
			if (gamepad1.dpad_left) {
				if(right != 1.0 && left != 0.0) {
					right += 0.01
					left -= 0.01
				}
			} else if (gamepad1.dpad_right) {
				if(right != 0.0 && left != 1.0) {
					right -= 0.01
					left += 0.01
				}
			}
			if(gamepad1.a) {
				stopper.position = 0.0
			} else if (gamepad1.b) {
				stopper.position = 0.2
			} else if (gamepad1.y) {
				stopper.position = 0.5
			}
			telemetry.addData("right", right)
			telemetry.addData("left", left)
			telemetry.update()
		}
	}
}
