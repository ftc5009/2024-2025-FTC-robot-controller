package org.firstinspires.ftc.teamcode

import ca.helios5009.hyperion.core.Motors
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap

class Robot(hardwareMap: HardwareMap) {
    val motor = Motors(hardwareMap, "FL", "FR", "BL", "BR")

}

@Config
object DriveConstants {
    @JvmField var GainSpeed = 0.075
    @JvmField var AccelerationLimit = 1.0
    @JvmField var Tolerance = 1.0
    @JvmField var Deadband = 0.75
}

@Config
object StrafeConstants {
    @JvmField var GainSpeed = 0.0825
    @JvmField var AccelerationLimit = 1.5
    @JvmField var Tolerance = 0.0
    @JvmField var Deadband = 0.75
}

@Config
object RotateConstants {
    @JvmField var GainSpeed = 0.01
    @JvmField var AccelerationLimit = 1.0
    @JvmField var Tolerance = 4.0
    @JvmField var Deadband = 1.0
}