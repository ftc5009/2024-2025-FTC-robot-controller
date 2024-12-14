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
import org.firstinspires.ftc.teamcode.components.Arm
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events_Chambers

@Autonomous(name = "Comp2_Chamber")
class Auto_chamber_SecondComp_v2: LinearOpMode() {
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val eventListener = Simple_events_Chambers(this)
        val bot = Robot(this, eventListener.listener, true)
        val path = bot.path
        val linearadjust = 1.0
        path.start(Point(11.0, 63.0 * linearadjust, 0.0, "init"))
        .wait("initialized")
        telemetry.addLine("INITIALIZED")
        telemetry.update()
        waitForStart()
        path.segment(Point(15.0, 63.0 * linearadjust, 0.0, "start_sample").setTolerance(6.0)
                ,Point(28.0,63.0 * linearadjust,0.0, "set_gear")
        )
        .wait("gear_set")
        .segment(Point(36.0, 63.0, 0.0,"first_chamber").setTolerance(4.0)
                ,Point(39.5,63.0 * linearadjust,0.0).setTolerance(3.5)
                ,Point(36.5, 63.0 * linearadjust, 0.0)
        )
        path.wait("first_chamber_ready")
        path.wait("first_score", "first_score_chamber")
        .segment(Point(32.5, 63.0 * linearadjust, 0.0)
                ,Point(25.0, 40.0 * linearadjust, -65.0).setTolerance(6.0)
                ,Point(52.0, 33.0 * linearadjust, -100.0).setTolerance(6.0)
                ,Point(12.0, 28.0 * linearadjust, -179.0, "ready_arm").setTolerance(6.0)
                ,Point(26.0, 25.0 * linearadjust, -179.0).setTolerance(4.0)
                ,Point(24.0, 25.0 * linearadjust, -179.0,"drop_arm")
        )
        .wait("picked_up")
        .segment(Point(20.0, 27.0,-90.0).setTolerance(4.0)
                ,Point(18.0, 30.0, -90.0,"arm_up")
        )
        .segment(Point(24.0, 63.0 * linearadjust, 0.0).setTolerance(4.0)
                ,Point(38.0, 73.0 * linearadjust, 0.0, "chamber_up").setTolerance(4.0)
                ,Point(36.0, 70.0 * linearadjust, 0.0))
        .wait("chamber_ready")
        .wait("first_score", "first_score_chamber")
        .segment(Point(25.0, 63.0 * linearadjust, 0.0,"_arm_off").setTolerance(4.0)
                ,Point(39.0, 30.0 * linearadjust, -85.0).setTolerance(6.0)
                ,Point(50.0, 20.0 * linearadjust, -100.0).setTolerance(6.0)
                ,Point(12.0, 28.0 * linearadjust, -135.0, "ready_arm").setTolerance(6.0)
                ,Point(26.0, 25.0 * linearadjust, -179.0).setTolerance(4.0)
                ,Point(24.0, 25.0 * linearadjust, -179.0,"drop_arm")
        )
        .wait("picked_up")
        .segment(Point(13.0, 26.0 * linearadjust, 90.0, "arm_off"))
        /*.segment(Point(30.0, 55.0 * linearadjust, -179.0, "_lift_arm"))
        //.wait("chamber_ready")
        //.wait("score", "score_chamber")
        .segment(Point(30.0, 18.0 * linearadjust, -75.0)
                ,Point(43.0, 10.0 * linearadjust, -90.0).setTolerance(6.0)
                ,Point(10.0, 18.0 * linearadjust, -90.0).setTolerance(6.0)
                ,Point(13.0, 26.0 * linearadjust, -90.0, "_drop_arm")
        )*/
        /*path.segment(Point(33.0,69.0 * linearadjust,0.0, "arm_up"))
        path.wait(1000.0)
        path.wait("up_arm")
        path.wait("dropping", "drop")
        path.segment(Point(33.0,69.0 * linearadjust,0.0, "drop_done"))
        path.wait("ahh")
        path.segment(Point(28.0, 69.0 * linearadjust, 0.0, "lift_down"))
        path.segment(Point(15.0, 25.0 * linearadjust, 0.0))*/
        .end()
    }
}