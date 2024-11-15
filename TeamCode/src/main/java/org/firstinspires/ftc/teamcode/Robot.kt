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
    @JvmField var GainSpeed = 0.037
    @JvmField var AccelerationLimit = 0.6
    @JvmField var Tolerance = 1.0
    @JvmField var Deadband = 0.25
}

@Config
object StrafeConstants {
    @JvmField var GainSpeed = 0.076
    @JvmField var AccelerationLimit = 0.8
    @JvmField var Tolerance = 1.5
    @JvmField var Deadband = 0.25
}

@Config
object RotateConstants {
    @JvmField var GainSpeed = 1.5
    @JvmField var AccelerationLimit = 0.5
    @JvmField var Tolerance = Math.PI/12
    @JvmField var Deadband = Math.PI/24
}