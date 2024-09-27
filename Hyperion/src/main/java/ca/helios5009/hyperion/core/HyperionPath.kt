package ca.helios5009.hyperion.core

import ca.helios5009.hyperion.misc.commands.Bezier
import ca.helios5009.hyperion.misc.commands.EventCall
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.misc.generateBezier
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

class HyperionPath(private val opMode: LinearOpMode, private val listener: EventListener) {
	var odometry: Odometry? = null
	var movement: Movement? = null

	fun start(origin: Point) {
		listener.call(origin.event.message)
		odometry?.setOrigin(origin.x, origin.y, origin.rot)
	}
	fun line(point: Point) {
		movement?.start(mutableListOf(point))
	}

	fun bezier(bezier: Bezier) {
		val generatedBezier = generateBezier(bezier.start, bezier.control[0], bezier.control[1], bezier.end)
		movement?.start(generatedBezier)
	}

	fun continuousLine(points: List<Point>) {
		movement?.start(points as MutableList<Point>)
	}

	fun end(event: EventCall) {
		listener.call(event.message)
		movement!!.stopMovement()
	}

	fun wait(time: Long, event: EventCall) {
		listener.call(event.message)
		val currentPosition = odometry!!.getLocation()
		val timer = ElapsedTime()
		while(opMode.opModeIsActive() && timer.milliseconds() < time) {
			movement?.goToPosition(currentPosition)
		}
	}

	fun wait(message: String, event: EventCall) {
		listener.call(event.message)
		val currentPosition = odometry!!.getLocation()
		while(opMode.opModeIsActive() && !listener.isInQueue(message)) {
			movement?.goToPosition(currentPosition)
		}
	}

}