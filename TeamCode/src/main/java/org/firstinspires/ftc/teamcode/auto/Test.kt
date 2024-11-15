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

@Autonomous(name = "Test")
class Test: LinearOpMode() {
    val eventListener = EventListener()
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener, motors, otos, false)
        path.setDriveConstants(DriveConstants.GainSpeed, DriveConstants.AccelerationLimit, DriveConstants.Tolerance, DriveConstants.Deadband)
        path.setStrafeConstants(StrafeConstants.GainSpeed, StrafeConstants.AccelerationLimit, StrafeConstants.Tolerance, StrafeConstants.Deadband)
        path.setRotateConstants(RotateConstants.GainSpeed, RotateConstants.AccelerationLimit, RotateConstants.Tolerance, RotateConstants.Deadband)
        waitForStart()
        path.start(Point(7.0,108.0,0.0))
        path.segment(
            Point(8.0,120.0,0.0),
            Point(10.0,108.0,0.0),
            Point(57.0,108.0,0.0).useError(),
            Point(57.0, 110.0,0.0),
            Point(20.0,120.0,0.0).useError(),
            Point(27.0, 160.0, 0.0),
            Point(57.0,108.0,0.0).useError(),
            Point(57.0,128.0,0.0),
            Point(10.0,120.0,0.0)
        )
        path.endHold("_")
    }
}