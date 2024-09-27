package ca.helios5009.hyperion.misc.commands

class Point(var x: Double, var y: Double, var rot: Double, val event: EventCall = EventCall("nothing")) {
	var type = PointType.Global
	var useError = false

	fun setLocal():Point {
		type = PointType.Relative
		return this
	}

	fun useError():Point {
		useError = true
		return this
	}
}

enum class PointType {
	Global, Relative
}