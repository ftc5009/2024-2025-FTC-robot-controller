package org.firstinspires.ftc.teamcode

//import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import kotlin.math.abs

class Robot(private val instance : LinearOpMode) {
	val fl: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "FL")
	val fr: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "FR")
	val br: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "BR")
	val bl: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "BL")

	val rightEncoder: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "LIFT")
	val leftEncoder: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "BL")
	val backEncoder: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "FL")
	init {
		fl.direction = DcMotorSimple.Direction.REVERSE
		fr.direction = DcMotorSimple.Direction.FORWARD
		bl.direction = DcMotorSimple.Direction.REVERSE
		br.direction = DcMotorSimple.Direction.FORWARD

//		leftEncoder.direction = DcMotorSimple.Direction.REVERSE
//		rightEncoder.direction = DcMotorSimple.Direction.FORWARD
//		backEncoder.direction = DcMotorSimple.Direction.

		leftEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		rightEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		backEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

		fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		fr.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		bl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		br.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

		fl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		fr.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		bl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		br.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
	}
}

//@Config
object OdometryValues {
	@JvmField var distanceBack = -5.25;
	@JvmField var distanceLeftRight = 11.5652;
}
//@Config
object DriveConstants {
	@JvmField var GainSpeed = 0.075
	@JvmField var AccelerationLimit = 1.0
	@JvmField var DefaultOutputLimit = 0.8
	@JvmField var Tolerance = 1.0
	@JvmField var Deadband = 0.75
}

//@Config
object StrafeConstants {
	@JvmField var GainSpeed = 0.0825
	@JvmField var AccelerationLimit = 1.5
	@JvmField var DefaultOutputLimit = 1.0
	@JvmField var Tolerance = 1.0
	@JvmField var Deadband = 0.75
}

//@Config
object RotateConstants {
	@JvmField var GainSpeed = 0.01
	@JvmField var AccelerationLimit = 1.0
	@JvmField var DefaultOutputLimit = 1.0
	@JvmField var Tolerance = 4.0
	@JvmField var Deadband = 1.0
}