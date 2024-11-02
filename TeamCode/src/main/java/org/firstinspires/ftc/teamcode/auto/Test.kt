package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.hardware.Otos
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.pathing.PathBuilder
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name = "Test")
class Test: LinearOpMode() {
    val eventListener = EventListener()
    lateinit var path: PathBuilder<Otos>
    override fun runOpMode() {
        val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
        val otos = Otos(hardwareMap, "OTOS")
        val path = PathBuilder(this, eventListener, motors, otos, false)
        path.setDriveConstants(0.075,1.0,1.0,0.75)
        path.setStrafeConstants(0.0825,1.5,1.0,0.75)
        path.setRotateConstants(0.01,1.0,Math.PI/12,1.0)
        waitForStart()
        path.start(Point(7.0,108.0,0.0))
        path.segment(
            Point(8.0,120.0,0.0),
            Point(10.0,108.0,0.0),
            Point(57.0,108.0,0.0).useError(),
            Point(57.0, 110.0,0.0),
            Point(20.0,120.0,0.0).useError(),
            Point(27.0, 160.0, 0.0),
            Point(57.0,108.0,0.0).useError(),
            Point(57.0,128.0,0.0),
            Point(10.0,120.0,0.0)
        )
        path.endHold("_")
    }
}