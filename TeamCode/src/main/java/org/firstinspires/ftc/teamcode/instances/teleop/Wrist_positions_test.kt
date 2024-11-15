package org.firstinspires.ftc.teamcode.instances.teleop

import android.util.Range
import ca.helios5009.hyperion.core.Motors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.components.Arm
import org.firstinspires.ftc.teamcode.components.Arm.Companion.gear_target
import org.firstinspires.ftc.teamcode.components.Arm.Companion.slide_target
import kotlin.math.max
import kotlin.math.min

@TeleOp(name = "Wrist test")
class Wrist_positions_test: LinearOpMode() {
	override fun runOpMode() {
		val left_wrist = hardwareMap.get(Servo::class.java, "Left_Wrist")
		val right_wrist = hardwareMap.get(Servo::class.java, "Right_Wrist")
		var twist = 0.0
		var wrist = 0.0
		val half_way = 180.0
		waitForStart()
		while (opModeIsActive()) {
			left_wrist.position = ((wrist+half_way) + (twist+half_way))/2.0/255.0
			right_wrist.position = ((wrist+half_way) - (twist+half_way))/2.0/255.0

			if (gamepad1.dpad_up){
				wrist = min(wrist + 1.0, half_way)
			} else if(gamepad1.dpad_down) {
				wrist = max(wrist - 1.0, -half_way)
			}

			if(gamepad1.dpad_left){
				twist = max(twist - 1.0, -half_way)
			}else if(gamepad1.dpad_right){
				twist =min(twist + 1.0, half_way)
			}

			telemetry.addData("twist", twist)
			telemetry.addData("wrist", wrist)
			telemetry.update()
		}
	}
}
