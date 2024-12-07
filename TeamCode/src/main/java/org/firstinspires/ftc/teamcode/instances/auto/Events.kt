package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.firstinspires.ftc.teamcode.components.Arm
import kotlin.concurrent.thread

class Events (instance:LinearOpMode){
    val listener = EventListener()
    val arm = Arm(instance)
    init {
        arm.init_motors()
        listener.addListener("lift_sample") {
            arm.intake_servos(0.0)
            if(arm.slide.getPosition() / arm.slide_inches_ticks > 8.0) {
                Arm.slide_target.set(8.0)
                delay(150)
            }
            if(Arm.gear_target.get() < 50.0){
                Arm.gear_target.set(50.0)
                delay(500)
            }else{
                Arm.gear_target.set(47.0)
            }
            while(arm.gear.getPosition() / arm.gear_degrees_ticks > 60){
                delay(50)
            }
            delay(200)
            Arm.slide_target.set(26.0)
            arm.wrist_servos(0.75, 0.75)
            while(arm.slide.getPosition() / arm.slide_inches_ticks < 24.0){
                delay(50)
            }
            "drop_sample"
        }
        listener.addListener("drop_sample") {
            Arm.gear_target.set(43.0)
            arm.intake_servos(-1.0)
            delay(400)
            arm.intake_servos(0.0)
            "finish_dropping_sample"
        }
        listener.addListener("lift_down_yellow") {
            Arm.slide_target.set(5.0)
            delay(500)
            Arm.gear_target.set(130.0)
            arm.wrist_servos(0.5, 0.5)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks < 115.0){
                delay(50)
            }
            "end_down"
        }
        listener.addListener("pickup_yellow"){
            //Arm.slide_target.set(0.0)
            Arm.gear_target.set(143.0)
            arm.intake_servos(1.0)
            delay(1000)
            "finish_pickup_yellow"
        }
        listener.addListener("final_lift_down_yellow"){
            Arm.slide_target.set(0.0)
            Arm.gear_target.set(130.0)
            arm.wrist_servos(0.64, 0.36)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks < 115.0){
                delay(50)
            }
            "_"
        }
        listener.addListener("final_pickup_yellow"){
            Arm.gear_target.set(135.0)
            arm.intake_servos(1.0)
            delay(700)
            "finished_final_pickup_yellow"
        }
        listener.addListener("park_ascend") {
            Arm.gear_target.set(73.0)
            Arm.slide_target.set(8.0)
            "help"
        }
        listener.addListener("start_sample") {
            while(instance.opModeIsActive() || instance.opModeInInit()){
                arm.go_to_target()
            }
            "started"
        }
        listener.addListener("ascend") {
            arm.gear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT)
            arm.gear.setPower(0.5)
            delay(500)
            arm.gear.setPower(0.0)
            delay(500)
            arm.gear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
            "_"
        }
    }







}