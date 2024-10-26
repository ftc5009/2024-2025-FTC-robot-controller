package org.firstinspires.ftc.teamcode.components

import ca.helios5009.hyperion.core.ProportionalController
import ca.helios5009.hyperion.hardware.HyperionMotor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import java.util.concurrent.atomic.AtomicReference

class Arm(private val instance:LinearOpMode) {
    var gear_target = AtomicReference(0.0)
    var slide_target = AtomicReference(0.0)
    val pid_gear = ProportionalController(0.1, 0.4, 0.4, 0.0, false)
    val pid_slide = ProportionalController(0.3,0.5,1.0,0.0, false)
    val gear_degrees_ticks = (100*384.5)/16/360
    val slide_inches_ticks = 384.5*25.4/120
    val gear = HyperionMotor(instance.hardwareMap, "GEAR")
    val slide = HyperionMotor(instance.hardwareMap, "SLIDE")
    init {
      slide.motor.direction = DcMotorSimple.Direction.REVERSE
    }
    fun go_to_target(){
        val gearOutput = pid_gear.update(gear_target.get() - gear.getPosition()/gear_degrees_ticks)
        val slideOutput = -pid_slide.update(slide_target.get() - slide.getPosition()/slide_inches_ticks)
        instance.telemetry.addData("Error Slide: ", slideOutput)
        //gear.setPower(gearOutput)
        slide.setPower(slideOutput)
    }
}