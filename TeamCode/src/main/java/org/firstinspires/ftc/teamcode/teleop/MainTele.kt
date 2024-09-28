package org.firstinspires.ftc.teamcode.teleop

//import ca.helios5009.hyperion.core.Odometry
//import com.acmerobotics.dashboard.FtcDashboard
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.firstinspires.ftc.teamcode.instances.teleop.MainTeleOp

@TeleOp(name = "Main", group = "Production")
class MainTele: LinearOpMode(){

	override fun runOpMode() {
		val instance = MainTeleOp(this)
		//val dashboard = FtcDashboard.getInstance()

		//telemetry = MultipleTelemetry(telemetry, dashboard.telemetry)

		waitForStart()

		val gamepadOneCoroutine = CoroutineScope(Dispatchers.Default).launch {
			while(opModeIsActive()) {
				instance.gamepadOne(gamepad1)
			}
		}

		val gamepadTwoCoroutine = CoroutineScope(Dispatchers.Default).launch {
			while(opModeIsActive()) {
				instance.gamepadTwo(gamepad2)
			}
		}

		while(opModeIsActive()) {
			//instance.odometry.calculate()
			//telemetry.addData("X", instance.odometry.getLocation().x)
			//telemetry.addData("Y", instance.odometry.getLocation().y)
			//telemetry.addData("Heading", instance.odometry.getRotDegrees())
			telemetry.update()
		}

		gamepadOneCoroutine.cancel()
		gamepadTwoCoroutine.cancel()
	}

}