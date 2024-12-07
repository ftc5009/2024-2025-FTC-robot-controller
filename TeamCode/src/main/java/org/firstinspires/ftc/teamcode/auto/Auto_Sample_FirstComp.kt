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

@Autonomous(name = "First_Comp")
class Auto_Sample_FirstComp: LinearOpMode() {
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val eventListener = Simple_events(this)
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener.listener, motors, otos, true)
        val linearadjust = 1.0

        waitForStart()
        path.start(Point(11.0, 110.0 * linearadjust, 0.0, "start_sample"))//start
        path.segment(Point(17.0,110.0 * linearadjust,0.0, "set_gear"))//place0
        path.wait(1000.0)
        path.wait("gear_set")
        path.segment(Point(17.0,127.0 * linearadjust,-41.0, "arm_up"))
        path.wait("up_arm")
        path.wait("dropping", "drop")
        path.segment(Point(19.0,124.0 * linearadjust,-80.0, "drop_done"))
        path.wait(1000.0)
        path.segment(

            Point(38.0, 110.0 * linearadjust, -80.0, "lift_down"),//park
            Point(55.0, 117.0 * linearadjust, -80.0)
        )
        path.segment(Point(56.0,103.0 * linearadjust,-80.0))
        path.wait(1000.0)
        path.segment(Point(56.0, 98.0 * linearadjust, -80.0))
        path.end("ascend")
    }
}