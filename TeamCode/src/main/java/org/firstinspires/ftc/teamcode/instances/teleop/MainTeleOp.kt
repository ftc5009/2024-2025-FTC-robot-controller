package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.core.Movement
import ca.helios5009.hyperion.core.Odometry
import ca.helios5009.hyperion.misc.PositionTracking
import ca.helios5009.hyperion.misc.PositionType
import ca.helios5009.hyperion.misc.events.EventListener
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
	val motors = Motors(bot.fl, bot.fr, bot.br, bot.bl)
	val eventListner = EventListener()
	val movement = Movement(instance, eventListner, motors, PositionTracking.OTOS)

	init {
		movement.setTracking(bot.otos, null)
	}

	fun gamepadOne(gamepad: Gamepad) {
		driveControls(gamepad)
	}

	fun gamepadTwo(gamepad: Gamepad) {

	}

	fun driveControls(gamepad: Gamepad) {
		val forward = -gamepad.left_stick_y.toDouble().pow(3) // * -sign(gamepad.left_stick_y.toDouble())
		val turn = (gamepad.right_stick_x.toDouble()).pow(3) //* sign(gamepad.right_stick_x.toDouble())
		val strafe = (gamepad.left_stick_x.toDouble() * 1.1).pow(3) //* sign(gamepad.left_stick_x.toDouble())

		motors.move(forward, strafe, turn)
	}
}

