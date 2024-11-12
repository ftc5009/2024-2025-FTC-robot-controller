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

@Autonomous(name = "Auto_Sample")
class Auto_Sample: LinearOpMode() {
    val eventListener = EventListener()
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener, motors, otos, true)
        path.setDriveConstants(DriveConstants.GainSpeed, DriveConstants.AccelerationLimit, DriveConstants.Tolerance, DriveConstants.Deadband)
        path.setStrafeConstants(StrafeConstants.GainSpeed, StrafeConstants.AccelerationLimit, StrafeConstants.Tolerance, StrafeConstants.Deadband)
        path.setRotateConstants(RotateConstants.GainSpeed, RotateConstants.AccelerationLimit, RotateConstants.Tolerance, RotateConstants.Deadband)
        waitForStart()
        path.start(Point(6.5,110.0 * (2.0/3.0),0.0))//start
        path.segment(Point(18.0, 136.0 * (2.0/3.0),0.0, "lift"),)//place0 done
        //path.wait("_finish_dropping")
        path.wait(3000.0)
        path.segment(Point(16.0,124.0 * (2.0/3.0),0.0, "lift_down"))//pickup1 done arm is 11in
        //path.wait("_finish_pickup")
        path.wait(1000.0)
        path.segment(Point(18.0,136.0 * (2.0/3.0),0.0, "lift"))//place1 done
        //path.wait("_finish_dropping")
        path.wait(3000.0)
        path.segment(Point(17.0,134.0 * (2.0/3.0),0.0, "lift_down"))//pickup2 done
        //path.wait("_finish_pickup")
        path.wait(1000.0)
        path.segment(Point(18.0,136.0 * (2.0/3.0),0.0, "lift"))//place2 done
        //path.wait("_finish_dropping")
        path.wait(3000.0)
        //path.segment(Point(21.5,126.6 * (2.0/3.0),0.0, "lift_down"))//pickup3
        //path.wait("_finish_pickup")
        //path.segment(Point(19.0,126.0 * (2.0/3.0),0.0, "lift"))//place3 done
        //path.wait("_finish_dropping")
        path.segment(
            Point(53.0, 120.0 * (2.0/3.0), 0.0),
            Point(53.0,120.0 * (2.0/3.0),90.0).useError(),
            Point(53.0,87.0 * (2.0/3.0),90.0)
            )//park
        path.endHold("_")
    }
}