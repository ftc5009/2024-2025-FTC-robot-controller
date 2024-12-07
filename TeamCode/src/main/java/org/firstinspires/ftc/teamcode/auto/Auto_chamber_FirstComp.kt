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
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events
import org.firstinspires.ftc.teamcode.instances.auto.Simple_events_Chambers

@Autonomous(name = "First_Comp_Chamber")
class Auto_chamber_FirstComp: LinearOpMode() {
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val eventListener = Simple_events_Chambers(this)
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener.listener, motors, otos, true)
        val linearadjust = 1.0

        waitForStart()
        path.start(Point(11.0, 69.0 * linearadjust, 0.0, "start_sample"))//start
        path.wait(2000.0)
        path.segment(Point(20.0,69.0 * linearadjust,0.0, "set_gear"))
        path.wait(1000.0)
        path.segment(Point(33.0, 69.0 * linearadjust, 0.0))
        path.wait(1000.0)
        path.wait("gear_set")
        path.segment(Point(33.0,69.0 * linearadjust,0.0, "arm_up"))
        path.wait(1000.0)
        path.wait("up_arm")
        path.wait("dropping", "drop")
        path.segment(Point(33.0,69.0 * linearadjust,0.0, "drop_done"))
        path.wait("ahh")
        path.segment(Point(28.0, 69.0 * linearadjust, 0.0, "lift_down"))
        path.segment(Point(15.0, 25.0 * linearadjust, 0.0))
        path.end("_")
    }
}