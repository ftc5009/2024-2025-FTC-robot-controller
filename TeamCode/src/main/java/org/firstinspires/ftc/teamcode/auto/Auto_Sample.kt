package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.hardware.Otos
import ca.helios5009.hyperion.pathing.PathBuilder
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.instances.auto.Events

@Autonomous(name = "Auto_Sample")
class Auto_Sample: LinearOpMode() {
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val eventListener = Events(this)
        val bot = Robot(this, eventListener.listener, true)
        val path = bot.path
        val linearadjust = 1.0

        path.start(Point(11.0, 110.0 * linearadjust, 0.0, "start_sample"))//start
        waitForStart()
        path.segment(
            Point(23.0,120.0 * linearadjust,0.0, "lift_sample"),//place0
            Point(18.0, 128.0 * linearadjust, 0.0))
        path.wait("finish_dropping_sample")
        path.segment(Point(23.0, 117.5 * linearadjust, 0.0, "lift_down_yellow"))//pickup1
        path.wait("end_down")
        path.wait("finish_pickup_yellow", "pickup_yellow")
        //path.wait(1000.0)
        path.segment(Point(18.0, 128.0 * linearadjust, 0.0, "lift_sample"))//place1
        path.wait("finish_dropping_sample")
        //path.wait(2000.0)
        path.segment(Point(23.0, 124.0 * linearadjust, 0.0, "lift_down_yellow")) //pickup2
        path.wait("end_down")
        path.wait("finish_pickup_yellow", "pickup_yellow")
        //path.wait(1000.0)
        path.segment(Point(18.0, 128.0 * linearadjust, 0.0, "lift_sample"))//place2
        path.wait("finish_dropping_sample")
        //path.wait(2000.0)
        path.segment(Point(30.0, 123.0 * linearadjust, 37.0, "final_lift_down_yellow"))//pickup3
        path.wait("finished_final_pickup_yellow", "final_pickup_yellow")
        path.segment(Point(17.0, 126.0 * linearadjust, -41.0, "lift_sample"))//place3
        path.wait("finish_dropping_sample")
        path.segment(
            Point(55.0, 120.0 * linearadjust, -41.0, "park_ascend"),//park
            Point(52.0, 115.0 * linearadjust, -90.0)
        )
        path.segment(Point(52.0, 98.0 * linearadjust, -90.0))
        path.end("ascend")
    }
}