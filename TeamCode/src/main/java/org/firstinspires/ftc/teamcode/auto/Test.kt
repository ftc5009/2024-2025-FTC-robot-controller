package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.core.Movement
import ca.helios5009.hyperion.misc.constants.PositionTracking
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.PathBuilder
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name = "Test")
class Test: LinearOpMode() {
    val eventListener = EventListener()
    override fun runOpMode() {
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val movement = Movement(this, eventListener, motors, PositionTracking.OTOS)
        val path = PathBuilder(movement)
        waitForStart()
        path.start(Point(0.0,0.0,0.0))
        path.segment(
            Point(96.0,0.0,0.0),
            Point(96.0, -30.0,0.0)
        )
        path.wait("2000ms")
        path.segment(
            Point(96.0,0.0,0.0)
        )
        path.end("_")
    }
}