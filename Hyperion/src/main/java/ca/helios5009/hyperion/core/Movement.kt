package ca.helios5009.hyperion.core

import android.net.ipsec.ike.exceptions.InvalidKeException
import ca.helios5009.hyperion.misc.PositionType
import ca.helios5009.hyperion.misc.euclideanDistance
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.commands.PointType
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Movement(
	private val opMode: LinearOpMode,
	private val listener: EventListener,
	private val bot: Motors,
	private val type: PositionType
) {
	private var otos: SparkFunOTOS? = null
	private var deadwheels: Odometry? = null
	private val t = opMode.telemetry

	private lateinit var driveController : ProportionalController
	private lateinit var strafeController : ProportionalController
	private lateinit var rotateController : ProportionalController
	var target: Point = Point(0.0, 0.0, 0.0)

	fun start(points: MutableList<Point>) {
		target = points.removeLast()
		val pos = getPosition()
		for (i in 0 until points.size) {
			var point = points[i]

			listener.call(point.event.message)
			if (point.type == PointType.Relative) {
				val localX = pos.x + point.x
				val localY = pos.y + point.y
				val localRot = pos.rot + point.rot
				point = Point(localX, localY, localRot)
			}

			val isBigger = euclideanDistance(pos, target) < euclideanDistance(point, target)
			goToPosition(point, isBigger)
			resetController()
		}
		listener.call(target.event.message)
		goToEndPoint()
		bot.stop()
	}
	fun setControllerConstants(drive: DoubleArray, strafe: DoubleArray, rotate: DoubleArray) {
		driveController = ProportionalController(drive[0], drive[1], drive[2], drive[3], drive[4])
		strafeController = ProportionalController(strafe[0], strafe[1], strafe[2], strafe[3], strafe[4])
		rotateController = ProportionalController(rotate[0], rotate[1], rotate[2], rotate[3], rotate[4], true)
	}

	fun goToPosition(nextPoint: Point, isBigger: Boolean? = null) {
		var pos = getPosition()
		while(opMode.opModeIsActive() && (isBigger == null || isBigger == euclideanDistance(pos, target) < euclideanDistance(nextPoint, target))) {

			val magnitude = euclideanDistance(nextPoint, pos)
			if (abs(magnitude) < 1.0) {
				t.addLine("Loop broke")
				t.update()
				break
			}

			val speedFactor = if (!nextPoint.useError) {
				euclideanDistance(pos, target)
			} else {
				euclideanDistance(pos, nextPoint)
			}

			val deltaX = (nextPoint.x - pos.x) / magnitude * speedFactor
			val deltaY = nextPoint.y - pos.y / magnitude * speedFactor
			val deltaRot = (nextPoint.rot - pos.rot)

			val theta = pos.rot
			val dx = deltaX * cos(theta) - deltaY * sin(theta)
			val dy = deltaX * sin(theta) + deltaY * cos(theta)

			val drive = driveController.getOutput(dx)
			val strafe = -strafeController.getOutput(dy)
			val rotate = -rotateController.getOutput(deltaRot * 180/PI)
			bot.move(drive, strafe, rotate)

////				t.addData("drivePosition", driveController.inPosition)
////				t.addData("strafePosition", strafeController.inPosition)
////				t.addData("turnPosition", rotateController.inPosition)
////				t.addLine("-----------------------------------------------")
////				t.addData("Next", "${nextPoint.x}, ${nextPoint.y}")
////				t.addData("Target", "${target.x}, ${target.y}")
////				t.addData("Current", "${odometry.location.x}, ${odometry.location.y}")
////				t.addData("Bigger?", isBigger)
////				t.addData("Euclidean Difference", euclideanDistance(odometry.location, target) < euclideanDistance(nextPoint, target))
////				t.addData("magnitude", magnitude)
////				t.addData("current step", currentStep)
////				t.addData("drive power", drive)
////				t.addData("strafe power", strafe)
////				t.addData("turn power", rotate)
////				t.addData("Current X", odometry.location.x)
////				t.addData("Current Y", odometry.location.y)
////				t.addData("Next X", nextPoint.x)
////				t.addData("Next Y", nextPoint.y)
////				t.addData("Magnitude", magnitude)
////				t.addData("Speed Factor", speedFactor)
////				t.addData("X Error", dx)
////				t.addData("Y Error", dy)
////				t.addData("Distance X", deltaX)
////				t.addData("Distance Y", deltaY)
////				t.addData("Rot Error", deltaRot)
				t.addData("x", pos.x)
				t.addData("y", pos.y)
				t.addData("y", pos.rot)
				t.update()
//			}
			pos = getPosition()
			if (isBigger == null) {
				break
			}
		}
	}

	private fun goToEndPoint() {
		val timer = ElapsedTime()
		val timeout = ElapsedTime()
		var inPosition = false
		resetController()
		while (opMode.opModeIsActive()) {
			goToPosition(target)

			if (driveController.inPosition && strafeController.inPosition && rotateController.inPosition) {
				if (timer.seconds() > 0.15) {
					break
				}
			} else {
				timer.reset()
			}

//			if (driveController.inPosition() || strafeController.inPosition() || rotateController.inPosition()) {
//				if (!inPosition) {
//					timeout.reset()
//					inPosition = true
//				}
//				if (timeout.seconds() > 0.5) {
//					break
//				}
//			} else {
//				inPosition = false
//			}


		}
	}

	fun setOtos(x: SparkFunOTOS){
		if(type == PositionType.Otos){
			otos = x
		}
	}
	fun setDeadwheels(x: Odometry){
		if(type == PositionType.Deadwheels){
			deadwheels = x
		}
	}
	fun getPosition(): Point {
		if(type == PositionType.Otos && otos != null){
			val pos = otos!!.position
			return Point(
				pos.x,
				pos.y,
				pos.h
			)
		} else if(type == PositionType.Deadwheels && deadwheels != null) {
			return deadwheels!!.calculate()
		} else {
			throw IllegalArgumentException("Need either Otos or Deadwheels")
		}
	}

	fun setPosition(point: Point){
		if(type == PositionType.Otos && otos != null){
			otos!!.position == SparkFunOTOS.Pose2D(
				point.x,
				point.y,
				point.rot
			)
		} else if(type == PositionType.Deadwheels && deadwheels != null) {
			return deadwheels!!.setOrigin(point)
		} else {
			throw IllegalArgumentException("Need either Otos or Deadwheels")
		}
	}

	fun stopMovement() {
		bot.stop()
	}

	private fun resetController() {
		driveController.reset()
		strafeController.reset()
		rotateController.reset()
	}
}