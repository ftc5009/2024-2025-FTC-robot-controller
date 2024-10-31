package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.hardware.Otos
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.PathBuilder
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name = "PID_Tuning_SLT")
class PID_Tuning_SLT: LinearOpMode() {
    val eventListener = EventListener()
    override fun runOpMode() {
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val otos = Otos(hardwareMap, "otos")
        val path = PathBuilder(this, eventListener, motors, otos, true)
        waitForStart()
        path.start(Point(32.5,8.0,0.0))

        path.segment(
            Point(45.0, 39.0, 90.0)
        )
        path.endHold("_")

    }
}
