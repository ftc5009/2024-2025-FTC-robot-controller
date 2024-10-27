package org.firstinspires.ftc.teamcode.components

import ca.helios5009.hyperion.core.ProportionalController
import ca.helios5009.hyperion.hardware.HyperionMotor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Arm(private val instance:LinearOpMode) {

    val front_extension = 20.0
    val back_extension = -16.0
    val reference_angle = 3/4*Math.PI

    val pid_gear = ProportionalController(0.2, 0.2, 0.4, 0.0, false)
    val pid_slide = ProportionalController(0.14,0.2,1.0,0.0, false)

    val gear_degrees_ticks = (100*384.5)/16.0/360.0
    val gear_ratio = 10.0/14.0
    val slide_inches_ticks = 384.5*25.4/120.0 //* gear_ratio

    val gear = HyperionMotor(instance.hardwareMap, "GEAR")
    val slide = HyperionMotor(instance.hardwareMap, "SLIDE")

    init {
        slide.motor.direction = DcMotorSimple.Direction.REVERSE
        gear.motor.direction = DcMotorSimple.Direction.FORWARD
    }
    fun init_motors(){
        slide.motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slide.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        gear.motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        gear.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        slide_target.set(0.3)
        gear_target.set(25.0)
    }
    fun go_to_target(){
        val gearOutput = pid_gear.update(Range.clip(gear_target.get(),0.0, 130.0) - gear.getPosition()/gear_degrees_ticks)
        val gear_angle = gear.getPosition()/gear_degrees_ticks
        val slideOutput = if(gear_angle > 75.0){
            pid_slide.update(min(front_extension, slide_target.get())*slide_inches_ticks - slide.getPosition())
        }
        else if(gear_angle < 20.0){
            pid_slide.update(min(abs(back_extension), slide_target.get())*slide_inches_ticks - slide.getPosition())
        }
        else{
            pid_slide.update(min(slide_target.get(), abs(back_extension/Math.cos(reference_angle - Math.toRadians(gear_angle)))*slide_inches_ticks - slide.getPosition()))
        }

        instance.telemetry.addData("Error Slide", slideOutput)
        instance.telemetry.addData("Error Gear", gearOutput)
        instance.telemetry.addData("thingy", abs(back_extension/Math.cos(reference_angle - Math.toRadians(gear_angle))))
        gear.setPower(gearOutput)
        slide.setPower(slideOutput)
    }
    companion object {
        val gear_target = AtomicReference(0.0)
        val slide_target = AtomicReference(0.0)
    }
}