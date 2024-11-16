package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.hardware.Otos
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.components.Arm

@TeleOp(name = "Position Everything")
class positions_everything: LinearOpMode() {
	override fun runOpMode() {
		val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
		val left_wrist = hardwareMap.get(Servo::class.java, "Left_Wrist")
		val right_wrist = hardwareMap.get(Servo::class.java, "Right_Wrist")
		val otos = Otos(hardwareMap, "OTOS")
		val arm = Arm(this)
		var left = 1.0
		var right = 1.0
		motors.setPowerRatio(1.0)
		otos.setPosition(Point(6.5,110.0,0.0))
		waitForStart()
		while (opModeIsActive()) {
			left_wrist.position = left
			right_wrist.position = right

			if (gamepad1.dpad_up){
				if(right != 1.0 && left != 1.0 && right + left < 2.0) {
					right += 0.01
					left += 0.01
				}
			} else if(gamepad1.dpad_down) {
				if(right != 0.0 && left != 0.0 && right + left >1.0){
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

			telemetry.addLine(otos.getPosition().toString())
			telemetry.addData("gear", arm.gear.getPosition() / arm.gear_degrees_ticks)
			telemetry.addData("slide", arm.slide.getPosition() / arm.slide_inches_ticks)
			telemetry.addData("right", right)
			telemetry.addData("left", left)
			telemetry.update()
		}
	}
}
