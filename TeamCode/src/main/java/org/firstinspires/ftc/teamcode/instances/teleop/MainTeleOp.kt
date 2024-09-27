package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Odometry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.OdometryValues
import org.firstinspires.ftc.teamcode.Robot
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sign

class MainTeleOp(private val instance : LinearOpMode) {
	val bot = Robot(instance)
	val odometry = Odometry(bot.leftEncoder, bot.rightEncoder, bot.backEncoder)
	init {
		odometry.setConstants(
			OdometryValues.distanceBack, OdometryValues.distanceLeftRight
		)
	}

	fun gamepadOne(gamepad: Gamepad) {
		driveControls(gamepad)
	}

	fun gamepadTwo(gamepad: Gamepad) {

	}

	fun driveControls(gamepad: Gamepad) {
		val forward = -gamepad.left_stick_y.toDouble().pow(3) // * -sign(gamepad.left_stick_y.toDouble())
		val turn = (gamepad.right_stick_x.toDouble() * 0.8).pow(3) //* sign(gamepad.right_stick_x.toDouble())
		val strafe = (gamepad.left_stick_x.toDouble() * 1.1).pow(3) //* sign(gamepad.left_stick_x.toDouble())

		val totalPower = abs(forward) + abs(turn) + abs(strafe)

		val denominator = max(0.8, totalPower)

		bot.fl.power = (forward + strafe + turn) / denominator
		bot.fr.power = (forward - strafe - turn) / denominator
		bot.bl.power = (forward - strafe + turn) / denominator
		bot.br.power = (forward + strafe - turn) / denominator
	}
}

