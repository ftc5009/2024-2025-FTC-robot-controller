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
import org.firstinspires.ftc.teamcode.instances.auto.Events

@Autonomous(name = "Auto_Sample")
class Auto_Sample: LinearOpMode() {
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val eventListener = Events(this)
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener.listener, motors, otos, true)
        val linearadjust = 1.0
        path.setDriveConstants(
            DriveConstants.GainSpeed,
            DriveConstants.AccelerationLimit,
            DriveConstants.Tolerance,
            DriveConstants.Deadband
        )
        path.setStrafeConstants(
            StrafeConstants.GainSpeed,
            StrafeConstants.AccelerationLimit,
            StrafeConstants.Tolerance,
            StrafeConstants.Deadband
        )
        path.setRotateConstants(
            RotateConstants.GainSpeed,
            RotateConstants.AccelerationLimit,
            RotateConstants.Tolerance,
            RotateConstants.Deadband
        )
        path.start(Point(6.5, 110.0 * linearadjust, 0.0, "start"))//start
        waitForStart()
        path.segment(
            Point(25.0,120.0 * linearadjust,0.0, "lift"),
            Point(18.0, 128.0 * linearadjust, 0.0))//place0
        path.wait("finish_dropping")
        path.segment(Point(23.0, 117.5 * linearadjust, 0.0, "lift_down"))//pickup1
        path.wait("end_down")
        path.wait("finish_pickup", "pickup")
        //path.wait(1000.0)
        path.segment(Point(18.0, 128.0 * linearadjust, 0.0, "lift"))//place1
        path.wait("finish_dropping")
        //path.wait(2000.0)
        path.segment(Point(23.0, 124.0 * linearadjust, 0.0, "lift_down")) //pickup2
        path.wait("end_down")
        path.wait("finish_pickup", "pickup")
        //path.wait(1000.0)
        path.segment(Point(18.0, 128.0 * linearadjust, 0.0, "lift"))//place2
        path.wait("finish_dropping")
        //path.wait(2000.0)
        path.segment(Point(30.0, 123.0 * linearadjust, 37.0, "final_lift_down"))//pickup3
        path.wait("finished_final_pickup", "final_pickup")
        path.segment(Point(17.0, 126.0 * linearadjust, -41.0, "lift"))//place3
        path.wait("finish_dropping")
        path.segment(
            Point(55.0, 120.0 * linearadjust, -41.0, "park"),//park
            Point(52.0, 115.0 * linearadjust, -90.0)
        )
        path.segment(Point(52.0, 98.0 * linearadjust, -90.0))
        path.endHold("ascend")
    }
}