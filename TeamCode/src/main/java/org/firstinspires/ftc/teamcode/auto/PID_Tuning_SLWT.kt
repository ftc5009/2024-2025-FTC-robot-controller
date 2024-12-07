package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Robot

@Autonomous(name = "PID_Tuning_SLWT")
class PID_Tuning_SLWT: LinearOpMode() {
    val eventListener = EventListener()
    override fun runOpMode() {
        val bot = Robot(this, eventListener, true)
        val path = bot.path
        waitForStart()
        path.start(Point(32.5,8.0,0.0))

        path.segment(
            Point(45.0, 34.0, 0.0),
            Point(17.0, 55.5, 179.0)
        )
        path.end("_")

    }
}
