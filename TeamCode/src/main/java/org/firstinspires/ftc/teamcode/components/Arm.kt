package org.firstinspires.ftc.teamcode.components

import androidx.appcompat.widget.SearchView
import ca.helios5009.hyperion.core.ProportionalController
import ca.helios5009.hyperion.hardware.HyperionMotor
import ca.helios5009.hyperion.misc.constants.Position
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class Arm(private val instance:LinearOpMode) {

    val front_extension = 20.0
    val back_extension = -16.0
    val reference_angle = 3/4*Math.PI

    val pid_gear = ProportionalController(0.07, 0.1, 0.4, 0.0, false)
    val pid_slide = ProportionalController(0.05,0.1,1.0,0.0, false)

    val gear_degrees_ticks = (100*384.5)/16.0/360.0
    val gear_ratio = 10.0/14.0
    val slide_inches_ticks = 384.5*25.4/120.0 //* gear_ratio

    val gear = HyperionMotor(instance.hardwareMap, "GEAR")
    val slide = HyperionMotor(instance.hardwareMap, "SLIDE")
    val left_wrist = instance.hardwareMap.get(Servo::class.java, "Left Wrist")
    val right_wrist = instance.hardwareMap.get(Servo::class.java, "Right Wrist")
    val intake_1 = instance.hardwareMap.get(CRServo::class.java, "Intake 1")
    val intake_2 = instance.hardwareMap.get(CRServo::class.java,"Intake 2")

    init {
        slide.motor.direction = DcMotorSimple.Direction.REVERSE
        gear.motor.direction = DcMotorSimple.Direction.REVERSE
    }
    fun init_motors(){
        slide.motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slide.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        gear.motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        gear.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        slide_target.set(0.3)
        gear_target.set(25.0)

        //left_wrist.position = 0.5
        //right_wrist.position = 0.5
        intake_1.power = 0.0
        intake_2.power = 0.0

    }

    fun go_to_target(){
        val gearOutput = pid_gear.update(Range.clip(gear_target.get(),0.0, 130.0)*gear_degrees_ticks - gear.getPosition())
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
        slide.setPower(Math.sqrt(abs(slideOutput)) * sign(slideOutput))
    }

    fun wrist_servos(wrist: Double, twist: Double){
        left_wrist.position = (wrist + twist)/2/180
        right_wrist.position = (wrist - twist)/2/180
    }

    fun intake_servos(power: Double){
        intake_1.power = power
        intake_2.power = -power
    }

    companion object {
        val gear_target = AtomicReference(0.0)
        val slide_target = AtomicReference(0.0)
    }
}