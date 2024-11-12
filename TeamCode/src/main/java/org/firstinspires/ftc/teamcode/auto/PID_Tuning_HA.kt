package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.hardware.Otos
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.PathBuilder
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.DriveConstants
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.RotateConstants
import org.firstinspires.ftc.teamcode.StrafeConstants

@Autonomous(name = "PID_Tuning_HA")
class PID_Tuning_HA: LinearOpMode() {
    val eventListener = EventListener()
    override fun runOpMode() {
        val bot = Robot(hardwareMap)
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener,bot.motor, otos, true)
        telemetry.addData("Drice constants", DriveConstants.GainSpeed)
        telemetry.update()
        path.setDriveConstants(DriveConstants.GainSpeed, DriveConstants.AccelerationLimit, DriveConstants.Tolerance, DriveConstants.Deadband)
        path.setStrafeConstants(StrafeConstants.GainSpeed, StrafeConstants.AccelerationLimit, StrafeConstants.Tolerance, StrafeConstants.Deadband)
        path.setRotateConstants(RotateConstants.GainSpeed, RotateConstants.AccelerationLimit, RotateConstants.Tolerance, RotateConstants.Deadband)
        waitForStart()
        path.start(Point(32.5,8.0,0.0))

        path.segment(
            Point(32.5,8.0,0.0)
        )
        path.wait(10000.0)
        path.endHold("_")

    }
}
