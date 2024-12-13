package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.hardware.Otos
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.PathBuilder
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.DriveConstants
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.RotateConstants
import org.firstinspires.ftc.teamcode.StrafeConstants
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events

@Autonomous(name = "Comp2_Sample")
class Auto_Sample_SecondComp: LinearOpMode() {
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val eventListener = Simple_events(this)
        val bot = Robot(this, eventListener.listener, true)
        val path = bot.path
        val linearadjust = 1.0
        val timer = ElapsedTime()
        eventListener.arm.wrist_servos(0.0,0.0)
        waitForStart()
        timer.reset()
        path.start(Point(11.0, 110.0 * linearadjust, 0.0, "start_sample"))//start
        .segment(Point(21.0,110.0 * linearadjust,0.0, "set_gear").setTolerance(4.0)
                ,Point(18.0, 105.0 * linearadjust, 0.0)
        )//place0
        path.wait("gear_set")
        .segment(Point(18.0,130.5 * linearadjust, -45.0, "arm_up").setTolerance(5.0)
                ,Point(18.0,126.0 * linearadjust,-45.0)
        )
        .wait("up_arm")
        .wait("dropped", "drop_sample")
        .segment(Point(28.0, 115.5, 0.0, "lift_down").setTolerance(4.0)
                ,Point(25.0, 119.0 * linearadjust, 0.0)
        )
        //.segment(Point(19.0,124.0 * linearadjust,-80.0, "drop_done"))
        .wait(100.0)
        .wait("picked_up", "pick_up") //pickup 1
        .segment(Point(18.0,130.5 * linearadjust, -45.0, "arm_up").setTolerance(4.0)
                ,Point(18.0,126.0 * linearadjust,-45.0)
        )
        .wait("up_arm")
        .wait("dropped", "drop_sample")
        .segment(Point(29.0, 129.5, 0.0, "lift_down").setTolerance(4.0)
                ,Point(26.0, 126.0 * linearadjust, 0.0)
        )
        //.segment(Point(19.0,124.0 * linearadjust,-80.0, "drop_done"))
        .wait(100.0)
        .wait("picked_up", "pick_up") //pickup 2
        .segment(Point(18.0,121.5 * linearadjust, -38.0, "arm_up").setTolerance(4.0)
                ,Point(18.0,125.0 * linearadjust,-38.0)
        )
        .wait("up_arm")
        .wait("dropped", "drop_sample")
        if(timer.seconds() < 24.0) {
            path.segment(Point(50.0,125.0 * linearadjust,90.0, "lift_down_final").setTolerance(4.0)
                    ,Point(48.0,135.0 * linearadjust,105.0).setTolerance(6.0)
                    ,Point(15.0,125.0 * linearadjust,135.0).setTolerance(4.0)
                    ,Point(40.0, 125.0,-170.0)
            )
        }
        path.segment(Point(56.0,120.0 * linearadjust,-80.0, "lift_down_final").setTolerance(6.0)
                ,Point(58.0, 94.0 * linearadjust, -80.0, "ascend").setTolerance(4.0)
                ,Point(58.0, 98.0 * linearadjust, -80.0)
        )
        .end()
    }
}