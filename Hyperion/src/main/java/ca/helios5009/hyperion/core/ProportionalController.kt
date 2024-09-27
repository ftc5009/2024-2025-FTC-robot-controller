package ca.helios5009.hyperion.core

import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import kotlin.math.abs

class ProportionalController(var gain: Double, var accelLimit: Double, var defaultOutputLimit: Double, tolerance: Double, deadband: Double, circular: Boolean = false) {

	var lastOutput = 0.0
	var liveOutputLimit: Double
	var setpoint = 0.0
	var tolerance: Double
	var deadband: Double
	var circular: Boolean
	var inPosition = false
	var cycleTime = ElapsedTime()

	init {
		liveOutputLimit = defaultOutputLimit
		this.tolerance = tolerance
		this.deadband = deadband
		this.circular = circular
		reset(0.0)
	}

	/**
	 * Determines power required to obtain the desired setpoint value based on new input value.
	 * Uses proportional gain, and limits rate of change of output, as well as max output.
	 * @param input  Current live control input value (from sensors)
	 * @return desired output power.
	 */
	fun getOutput(input: Double): Double {
		var error = input
		val dV = cycleTime.seconds() * accelLimit
		var output: Double

		// normalize to +/- 180 if we are controlling heading
		if (circular) {
			while (error > 180) error -= 360.0
			while (error <= -180) error += 360.0
		}
		inPosition = abs(error) < tolerance
		// Prevent any very slow motor output accumulation
		if (abs(error) <= deadband) {
			output = 0.0
		} else {
			// calculate output power using gain and clip it to the limits
			output = error * gain

			// Now limit rate of change of output (acceleration)
			if (abs(output) > abs(lastOutput)) {
				if (output - lastOutput > dV) {
					output = lastOutput + dV
				} else if (output - lastOutput < -dV) {
					output = lastOutput - dV
				}
			}
		}
		lastOutput = output
		cycleTime.reset()
		return output
	}

	fun inPosition(): Boolean {
		return inPosition
	}

	/**
	 * Saves a new setpoint and resets the output power history.
	 * This call allows a temporary power limit to be set to override the default.
	 * @param setPoint
	 * @param powerLimit
	 */
	fun reset(setPoint: Double, powerLimit: Double) {
		liveOutputLimit = abs(powerLimit)
		setpoint = setPoint
		reset()
	}

	/**
	 * Saves a new setpoint and resets the output power history.
	 * @param setPoint
	 */
	fun reset(setPoint: Double) {
		liveOutputLimit = defaultOutputLimit
		setpoint = setPoint
		reset()
	}

	/**
	 * Leave everything else the same, Just restart the acceleration timer and set output to 0
	 */
	fun reset(everything: Boolean = false) {
		cycleTime.reset()
		inPosition = false
		if (everything) {
			lastOutput = 0.0
		}
	}
}