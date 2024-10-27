package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.misc.constants.PositionTracking
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.PathBuilder
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name = "PID_Tuning_HA")
class PID_Tuning_HA: LinearOpMode() {
    val eventListener = EventListener()
    override fun runOpMode() {
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val path = PathBuilder(this, eventListener, motors, PositionTracking.OTOS)
        waitForStart()
        path.start(Point(32.5,8.0,0.0))

        path.segment(
            Point(32.5,8.0,0.0)
        )
        path.wait(30000.0)
        path.end("_")

    }
}
