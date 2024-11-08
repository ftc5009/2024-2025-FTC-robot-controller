package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.hardware.Otos
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
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener, motors, otos, true)
        path.setDriveConstants(0.075,1.0,1.0,0.75)
        path.setStrafeConstants(0.0825,1.5,1.0,0.75)
        path.setRotateConstants(0.01,1.0,Math.PI/12,1.0)
        waitForStart()
        path.start(Point(32.5,8.0,0.0))

        path.segment(
            Point(32.5,8.0,0.0)
        )
        path.wait(10000.0)
        path.endHold("_")

    }
}
