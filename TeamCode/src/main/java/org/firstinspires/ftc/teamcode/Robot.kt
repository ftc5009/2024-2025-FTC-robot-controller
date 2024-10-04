package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import ca.helios5009.hyperion.core.MotorWrapper
import ca.helios5009.hyperion.misc.euclideanDistance
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import kotlin.math.abs

class Robot(private val instance : LinearOpMode) {
	val fl: MotorWrapper = MotorWrapper("FL",instance.hardwareMap)
	val fr: MotorWrapper = MotorWrapper("FR",instance.hardwareMap)
	val br: MotorWrapper = MotorWrapper("BR",instance.hardwareMap)
	val bl: MotorWrapper = MotorWrapper("BL",instance.hardwareMap)

	val otos = instance.hardwareMap.get(SparkFunOTOS::class.java, "OTOS")

	init {
		fl.motor.direction = DcMotorSimple.Direction.REVERSE
		fr.motor.direction = DcMotorSimple.Direction.FORWARD
		bl.motor.direction = DcMotorSimple.Direction.REVERSE
		br.motor.direction = DcMotorSimple.Direction.FORWARD

		fl.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		fr.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		bl.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		br.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

		fl.motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		fr.motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		bl.motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		br.motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

		otos.linearUnit = DistanceUnit.INCH
		otos.angularUnit = AngleUnit.RADIANS

		otos.offset = SparkFunOTOS.Pose2D(0.0,0.0,0.0)
		otos.linearScalar = 1.0
		otos.angularScalar = 1.0

		otos.calibrateImu()
		otos.resetTracking()
	}
}

@Config
object OdometryValues {
	@JvmField var distanceBack = -5.25;
	@JvmField var distanceLeftRight = 11.5652;
}
@Config
object DriveConstants {
	@JvmField var GainSpeed = 0.075
	@JvmField var AccelerationLimit = 1.0
	@JvmField var DefaultOutputLimit = 0.8
	@JvmField var Tolerance = 1.0
	@JvmField var Deadband = 0.75
}

@Config
object StrafeConstants {
	@JvmField var GainSpeed = 0.0825
	@JvmField var AccelerationLimit = 1.5
	@JvmField var DefaultOutputLimit = 1.0
	@JvmField var Tolerance = 0.0
	@JvmField var Deadband = 0.75
}

@Config
object RotateConstants {
	@JvmField var GainSpeed = 0.01
	@JvmField var AccelerationLimit = 1.0
	@JvmField var DefaultOutputLimit = 1.0
	@JvmField var Tolerance = 4.0
	@JvmField var Deadband = 1.0
}