package ca.helios5009.hyperion.core

import ca.helios5009.hyperion.misc.PositionTracking
import ca.helios5009.hyperion.misc.euclideanDistance
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.commands.PointType
import ca.helios5009.hyperion.misc.cosineLaw
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Class that handles the movement of the robot
 *  - Set the constants for the [PID controllers][Movement.setControllerConstants]
 *  - Set the [tracking] method for the robot and run [setTracking] to add the odometry calculation
 *  - [mimimun path tolerence][Movement.minimumVectorTolerance] for the robot to reach the point in a continuous path, how much leeway the robot has to reach the point
 *  - [timeout] when the robot is stuck, how long the robot should wait before moving on
 *
 *  Other methods include (Don't use this method directly, use [HyperionPath] methods instead):
 *  - [run] the path that is given to the robot
 *  - [goto] the point that is given to the robot
 * @param opMode The LinearOpMode that the robot is running on
 * @param listener The EventListener that is used to call events
 * @param bot The Motors object that is used to move the robot
 * @param tracking The tracking method that is used to track the robot's position
 * @constructor Create a new Movement object
 * @see Motors
 * @see EventListener
 */
class Movement(
	private val opMode: LinearOpMode,
	private val listener: EventListener,
	private val bot: Motors,
	private val tracking: PositionTracking,
) {
	var minimumVectorTolerance: Double = 2.0
	var timeout = 150.0

	private lateinit var driveController : ProportionalController
	private lateinit var strafeController : ProportionalController
	private lateinit var rotateController : ProportionalController

	private var deadwheels: Odometry? = null
	private var otos: SparkFunOTOS? = null

	private var finalPathPoint: Point = Point(0.0, 0.0, 0.0)
	private var currentTargetPoint: Point = Point(0.0, 0.0, 0.0)
	private var path: List<Point> = listOf()
	private var currentPathIndex = 0;
	private var currentPosition = Point(0.0, 0.0, 0.0)

	fun run(points: List<Point>) {
		path = points // Set the path to the list of points
		val finalPoint = points.last() // Get the final point in the path
		finalPathPoint = if (finalPoint.type == PointType.Global) {
			finalPoint
		} else {
			currentPosition = getPosition()
			Point(
				finalPoint.x + currentPosition.x,
				finalPoint.y + currentPosition.y,
				finalPoint.rot + currentPosition.rot
			)
		}
		// Set the final path point to the last point in the list

		for (i in 0 until path.size - 1) { // Loop through the path but leave the last point
			currentTargetPoint = points[i] // Set the current target point to the current point in the path
			currentPosition = getPosition() // Get the current position of the robot
			currentPathIndex = i // Set the path index to the current point in the path

			val vectorTolerance = if (currentTargetPoint.useManualTorence) {
				currentTargetPoint.tolerence // Use the manual tolerance if it is set
			} else {
				val distanceA = euclideanDistance(currentPosition, currentTargetPoint) // Calculate the distance between the current position and the target point
				val distanceB = euclideanDistance(currentTargetPoint, points[i + 1]) // Calculate the distance between the target point and the next point
				val distanceC = euclideanDistance(currentPosition, points[i + 1]) // Calculate the distance between the current position and the next point

				val angleOfPath = cosineLaw(distanceA, distanceB, distanceC) // Calculate the angle of the path
				maxOf(minimumVectorTolerance, (Math.PI - angleOfPath) * minimumVectorTolerance) // Calculate the vector tolerance
			}

			listener.call(currentTargetPoint.event.message) // Call events
			resetController() // Reset the PID controllers
			while(
				opMode.opModeIsActive() &&
				euclideanDistance(currentPosition, currentTargetPoint) > vectorTolerance // Check if the robot is close to the target point
			) {
				goto(currentTargetPoint) // Move the robot closer to the target point
			}
		}
		listener.call(finalPathPoint.event.message) // Call the event at the final point
		goToEndPoint() // Move the robot to the final point
		bot.stop()
		path = listOf()
	}

	/**
	 * Set the constants for the PID controllers.
	 *
	 * The constants are in the following order:
	 * [Gain][ProportionalController.gain],
	 * [Acceleration Limit][ProportionalController.accelLimit],
	 * [Default Output Limit][ProportionalController.defaultOutputLimit]
	 * [Tolerance][ProportionalController.tolerance],
	 * [Deadband][ProportionalController.deadband],
	 *
	 * @param drive The constants for the drive controller
	 * @param strafe The constants for the strafe controller
	 * @param rotate The constants for the rotate controller (Set in Radians)
	 *
	 * @see ProportionalController
	 */
	fun setControllerConstants(drive: DoubleArray, strafe: DoubleArray, rotate: DoubleArray) {
		driveController = ProportionalController(drive[0], drive[1], drive[2], drive[3], drive[4])
		strafeController = ProportionalController(strafe[0], strafe[1], strafe[2], strafe[3], strafe[4])
		rotateController = ProportionalController(rotate[0], rotate[1], rotate[2], rotate[3], rotate[4], true)
	}

	/**
	 * Set power to the motors to move the robot to a point.
	 * Calculates the power needed to give to each motors (ONLY FOR [MECANUM](<en.wikipedia.org/wiki/Mecanum_wheel>)) WHEELS
	 * @param point The point to move the robot to
	 * @param endPoint If it is the end point
	 *
	 * @see Point
	 * @see ProportionalController.getOutput
	 */
	fun goto(point: Point, endPoint: Boolean = false) {
		currentPosition = getPosition() // Get the current position of the robot

		// Calculate the error between the target and the current position
		val error = euclideanDistance(point, currentPosition)

		// Calculate the speed factor (The speed that the robot should go to remove the stutter between points)
		val speedFactor = lookForNextError(currentPosition, endPoint)

		val deltaX = (point.x - currentPosition.x) / error * speedFactor
		val deltaY = (point.y - currentPosition.y) / error * speedFactor
		val deltaRot = point.rot - currentPosition.rot

		val theta = currentPosition.rot
		// Calculate the delta x amount that the robot should move
		val dx = deltaX * cos(theta) - deltaY * sin(theta)
		// Calculate the delta y amount that the robot should move
		val dy = deltaX * sin(theta) + deltaY * cos(theta)

		val drive = driveController.getOutput(dx)
		val strafe = -strafeController.getOutput(dy)
		val rotate = -rotateController.getOutput(deltaRot)
		bot.move(drive, strafe, rotate)
	}

	/**
	 * Look for the next error in the path. This is used to calculate the speed factor
	 * When looking for the next error, it use's the point after the error point to control it's distance.
	 * Best practice to set point that will act as a reference point after the error if you want to control the speed sooner or later.
	 * @param position The current position of the robot
	 * @param endPoint If it is the end point
	 * @return The distance to the next error
	 *
	 * @see Point
	 */
	private fun lookForNextError(position: Point, endPoint: Boolean): Double {
		var distance = 0.0
		var point = position // initialize with position

		for (i in currentPathIndex until path.size) { // Range
			val nextPoint = path[i] // Get the next point in the path
			val error = euclideanDistance(nextPoint, point) // Calculate the error between the next point and the current point
			distance += error // Add the error to the distance
			point = nextPoint // Set the current point to the next point
			if (point.useError && !endPoint) { // Check if the point uses error and if it is not the end point
				val nextNextPoint = path[i + 1] // Get the next point after the error point
				val nextError = euclideanDistance(nextNextPoint, point) // Calculate the error between the next point and the current point
				distance += nextError // Add the error to the distance
				break
			}
		}

		return distance
	}

	/**
	 * Move the robot to the end point.
	 * This is used to make sure that the robot reaches the end point.
	 * It will keep moving as long as one of the controllers have been to the ready position.
	 *
	 * @see Point
	 * @see ProportionalController
	 */
	private fun goToEndPoint() {
		val timeoutTimer = ElapsedTime() // Create a timer to timeout if the robot is stuck

		var inDrivePosition = false
		var inStrafePosition = false
		var inRotatePosition = false
		resetController()
		while (opMode.opModeIsActive()) {
			goto(finalPathPoint, true)
			if (inDrivePosition && inStrafePosition && inRotatePosition) {
				if (
					timeoutTimer.milliseconds() > timeout
					|| (driveController.inPosition && strafeController.inPosition && rotateController.inPosition)
				) {
					break
				}
			} else {
				timeoutTimer.reset()
			}

			if (driveController.inPosition && !inDrivePosition) {
				inDrivePosition = true
			}
			if (strafeController.inPosition && !inStrafePosition) {
				inStrafePosition = true
			}
			if (rotateController.inPosition && !inRotatePosition) {
				inRotatePosition = true
			}
		}
	}

	fun stopMovement() {
		bot.stop()
	}

	fun setTracking(otos: SparkFunOTOS?, deadwheels: Odometry?) {
		if (deadwheels == null && otos == null) {
			throw NullPointerException("You need to set a tracking method")
		}
		if (tracking == PositionTracking.OTOS && otos != null) {
			this.otos = otos
			otos.initialize()
		} else if (tracking == PositionTracking.DEADWHEELS && deadwheels != null) {
			this.deadwheels = deadwheels
		} else {
			throw IllegalArgumentException("Tracking is not set")
		}
	}

	fun getPosition(): Point {
		if (tracking == PositionTracking.OTOS && otos != null) {
			val position = otos!!.position
			return Point(
				position.x,
				position.y,
				position.h
			)
		} else if (tracking == PositionTracking.DEADWHEELS && deadwheels != null) {
			return deadwheels!!.calculate()
		} else {
			throw IllegalArgumentException("Tracking is not set")
		}
	}

	fun setPosition(point: Point) {
		if (tracking == PositionTracking.OTOS && otos != null) {
			otos!!.position = SparkFunOTOS.Pose2D(point.x, point.y, point.rot)
		} else if (tracking == PositionTracking.DEADWHEELS && deadwheels != null) {
			deadwheels!!.setOrigin(point)
		} else {
			throw IllegalArgumentException("Tracking is not set")
		}
	}

	private fun resetController() {
		driveController.reset()
		strafeController.reset()
		rotateController.reset()
	}


}