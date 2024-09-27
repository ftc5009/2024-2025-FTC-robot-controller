package ca.helios5009.hyperion.core

import ca.helios5009.hyperion.misc.euclideanDistance
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.commands.PointType
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Movement(private val listener: EventListener, private val bot: Motors, private val odometry: Odometry, private val opMode: LinearOpMode) {

	private lateinit var driveController : ProportionalController
	private lateinit var strafeController : ProportionalController
	private lateinit var rotateController : ProportionalController
	var target: Point = Point(0.0, 0.0, 0.0)

	fun start(points: MutableList<Point>) {
		target = points.removeLast()
		for (i in 0 until points.size) {
			var point = points[i]

			if (point.event.message.startsWith("constant_move")) { // "constant_move|90|nothing"
				val split = point.event.message.split("|")
				val angle = split[1].toDouble() * PI / 180.0
				val message = split[2]
				listener.call(message)
				while(opMode.opModeIsActive() && odometry.calculate() && abs(odometry.getLocation().rot - angle) > 1.0) {
					bot.move(point.x, point.y, point.rot)
				}
			} else {
				listener.call(point.event.message)
				if (point.type == PointType.Relative) {
					val localX = odometry.getLocation().x + point.x
					val localY = odometry.getLocation().y + point.y
					val localRot = odometry.getLocation().rot + point.rot
					point = Point(localX, localY, localRot)
				}

			}
			val isBigger = euclideanDistance(odometry.getLocation(), target) < euclideanDistance(point, target)
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
		while(opMode.opModeIsActive() && odometry.calculate() && (isBigger == null || isBigger == euclideanDistance(odometry.getLocation(), target) < euclideanDistance(nextPoint, target))) {

			val magnitude = euclideanDistance(nextPoint, odometry.getLocation())
			if (abs(magnitude) < 1.0) {
				println("magnitude broke loop")
				break
			}

			val speedFactor = if (!nextPoint.useError) {
				euclideanDistance(odometry.getLocation(), target)
			} else {
				euclideanDistance(odometry.getLocation(), nextPoint)
			}

			val deltaX = -(nextPoint.x - odometry.getLocation().x) / magnitude * speedFactor
			val deltaY = nextPoint.y - odometry.getLocation().y / magnitude * speedFactor
			val deltaRot = -(nextPoint.rot - odometry.getLocation().rot)

			val theta = -odometry.getLocation().rot
			val dx = deltaX * cos(theta) - deltaY * sin(theta)
			val dy = deltaX * sin(theta) + deltaY * cos(theta)

			val drive = driveController.getOutput(dx)
			val strafe = strafeController.getOutput(dy)
			val rotate = rotateController.getOutput(deltaRot * 180/PI)
			bot.move(drive, strafe, rotate)
//			if (t != null) {
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
////				t.update()
//			}
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

			if (driveController.inPosition() || strafeController.inPosition() || rotateController.inPosition()) {
				if (!inPosition) {
					timeout.reset()
					inPosition = true
				}
				if (timeout.seconds() > 0.5) {
					break
				}
			} else {
				inPosition = false
			}


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