package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.hardware.Otos
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.PathBuilder
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.DriveConstants
import org.firstinspires.ftc.teamcode.RotateConstants
import org.firstinspires.ftc.teamcode.StrafeConstants
import org.firstinspires.ftc.teamcode.components.Arm.Companion.slide_target

@Autonomous(name = "PID_Tuning_Diagon")
class PID_Tuning_Diagon: LinearOpMode() {
    val eventListener = EventListener()
    override fun runOpMode() {
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener, motors, otos, true)
        path.setDriveConstants(DriveConstants.GainSpeed, DriveConstants.AccelerationLimit, DriveConstants.Tolerance, DriveConstants.Deadband)
        path.setStrafeConstants(StrafeConstants.GainSpeed, StrafeConstants.AccelerationLimit, StrafeConstants.Tolerance, StrafeConstants.Deadband)
        path.setRotateConstants(RotateConstants.GainSpeed, RotateConstants.AccelerationLimit, RotateConstants.Tolerance, RotateConstants.Deadband)
        waitForStart()
        path.start(Point(0.0, 0.0, 0.0))

        path.segment(
            Point(7.0, 10.0, 0.0)
        )
        path.endHold("_")

    }
}
