package org.firstinspires.ftc.teamcode.auto

import ca.helios5009.hyperion.core.HyperionPath
import ca.helios5009.hyperion.misc.commands.EventCall
import ca.helios5009.hyperion.misc.commands.Point
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.instances.auto.MainAuto

@Autonomous(name = "Therapy")
class Therapy: LinearOpMode() {

    override fun runOpMode() {
        val instance = MainAuto(this)
        val paths = HyperionPath(this, instance.eventListner, instance.movement)

        waitForStart()
        if (opModeIsActive()) {
            paths.start(Point(0.0,0.0,0.0))
            paths.continuousLine(
                mutableListOf(
                    Point(90.0, 0.0, 0.0),
                    Point(90.0,-30.0,0.0)
                )
            )
            paths.end(EventCall(""))

        }
            while(opModeIsActive()) {
            val pos = instance.movement.getPosition()
            telemetry.addData("x", pos.x)
            telemetry.addData("y", pos.y)
            telemetry.addData("rot", pos.rot)
            telemetry.update()
        }
    }
}