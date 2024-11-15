package org.firstinspires.ftc.teamcode.instances.teleop

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.hardware.Otos
import ca.helios5009.hyperion.pathing.Point
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.components.Arm
import org.firstinspires.ftc.teamcode.components.Arm.Companion.gear_target
import org.firstinspires.ftc.teamcode.components.Arm.Companion.slide_target

@TeleOp(name = "rdftgyhuij")
class yfdgtryui: LinearOpMode() {
	override fun runOpMode() {
		val motors = Motors(hardwareMap, "FL", "FR", "BL", "BR")
		val otos = Otos(hardwareMap, "OTOS")
		var twist = 0.0
		var wrist = 0.0
		val arm = Arm(this)
		motors.setPowerRatio(1.0)
		otos.setPosition(Point(0.0,0.0,0.0))
		waitForStart()
		while (opModeIsActive()) {
			telemetry.addLine(otos.getPosition().toString())
			telemetry.addData("gear", arm.gear.getPosition() / arm.gear_degrees_ticks)
			telemetry.addData("slide", arm.slide.getPosition() / arm.slide_inches_ticks)
			telemetry.addData("twist", twist)
			telemetry.addData("wrist", wrist)
			telemetry.update()
		}
	}
}
