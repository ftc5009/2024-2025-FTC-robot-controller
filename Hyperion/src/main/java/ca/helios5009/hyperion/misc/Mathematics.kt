package ca.helios5009.hyperion.misc

import ca.helios5009.hyperion.misc.commands.Point
import kotlin.math.pow
import kotlin.math.sqrt

fun euclideanDistance(p1: Point, p2: Point): Double {
	return sqrt((p2.x - p1.x).pow(2.0) + (p2.y - p1.y).pow(2.0))
}

fun cubicBezier(p0: Double, p1: Double, p2: Double, p3: Double, t: Double): Double {
	return (1 - t).pow(3) * p0 + 3 * (1 - t).pow(2) * t * p1 + 3 * (1 - t) * t.pow(2) * p2 + t.pow(3) * p3
}

fun lerp(p0: Double, p1: Double, t: Double): Double {
	return p0 + t * (p1 - p0)
}

fun generateBezier(pt0: Point, ct0: Point, ct1: Point, pt1: Point): MutableList<Point> {
	val points = mutableListOf<Point>()
	val resolution = 20

	for (i in 0..resolution) {
		val t = i.toDouble() / resolution

		val x = cubicBezier(pt0.x, ct0.x, ct1.x, pt1.x, t)

		val y = cubicBezier(pt0.y, ct0.y, ct1.y, pt1.y, t)

		val rot = if (lerp(pt0.rot, pt1.rot, t) > 0)
			lerp(pt0.rot, pt1.rot, t)
		else
			lerp(pt0.rot, pt1.rot, t) + 360

		when(t) {
			0.0 -> points.add(Point(x, y, 0.0, pt0.event))
			1.0 -> points.add(Point(x, y, 0.0, pt1.event))
			0.25 -> points.add(Point(x, y, 0.0, ct0.event))
			0.75 -> points.add(Point(x, y, 0.0, ct1.event))
			else -> points.add(Point(x, y, 0.0))
		}
	}

	return points
}