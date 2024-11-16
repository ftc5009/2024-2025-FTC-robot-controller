package org.firstinspires.ftc.teamcode.instances.teleop

import android.util.Range
import ca.helios5009.hyperion.core.Motors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.components.Arm
import org.firstinspires.ftc.teamcode.components.Arm.Companion.gear_target
import org.firstinspires.ftc.teamcode.components.Arm.Companion.slide_target
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@TeleOp(name = "Wrist test")
class Wrist_positions_test: LinearOpMode() {
	override fun runOpMode() {
		val left_wrist = hardwareMap.get(Servo::class.java, "Left_Wrist")
		val right_wrist = hardwareMap.get(Servo::class.java, "Right_Wrist")
		var left = 1.0
		var right = 1.0
		val half_way = 180.0
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
			telemetry.addData("right", right)
			telemetry.addData("left", left)
			telemetry.update()
		}
	}
}
