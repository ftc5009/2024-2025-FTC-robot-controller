package ca.helios5009.hyperion.core

import com.qualcomm.robotcore.hardware.DcMotorEx
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs


class Motors(val fl: DcMotorEx, val fr: DcMotorEx, val br: DcMotorEx, val bl: DcMotorEx) {
	val powerRatio = AtomicReference(1.0)

	fun move(drive: Double, strafe: Double, rotate: Double) {
		val maxPower = abs(drive) + abs(strafe) + abs(rotate)
		val max = if (maxPower < 0.15) maxPower / 0.15 else maxOf(1.0, maxPower/0.8)/powerRatio.get() // 0.8 is the max power of the motors, if the number is greater than 0.8, it will be divided by 0.8

		fl.power = (drive - strafe + rotate) / max
		fr.power = (drive + strafe - rotate) / max
		bl.power = (drive - strafe - rotate) / max
		br.power = (drive + strafe + rotate) / max
	}

	fun stop () {
		fl.power = 0.0
		fr.power = 0.0
		bl.power = 0.0
		br.power = 0.0
	}
}