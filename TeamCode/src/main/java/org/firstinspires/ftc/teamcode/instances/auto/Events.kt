package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
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
        listener.addListener("lift") {
            //arm.intake_servos(0.0)
            if(arm.slide.getPosition() / arm.slide_inches_ticks > 8.0) {
                Arm.slide_target.set(8.0)
                delay(150)
            }
            Arm.gear_target.set(43.0)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks > 60){
                delay(50)
            }
            delay(200)
            Arm.slide_target.set(26.0)
            arm.wrist_servos(30.0, -84.0)
            while(arm.slide.getPosition() / arm.slide_inches_ticks < 24.0){
                delay(50)
            }
            "drop"
        }
        listener.addListener("drop") {
            Arm.gear_target.set(40.0)
            //arm.intake_servos(1.0)
            delay(200)
            //arm.intake_servos(0.0)
            "finish_dropping"
        }
        listener.addListener("lift_down") {
            Arm.slide_target.set(5.0)
            delay(200)
            Arm.gear_target.set(130.0)
            arm.wrist_servos(90.0, 0.0)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks < 115.0){
                delay(50)
            }
            delay(200)
            "finish_pickup"
        }
        listener.addListener("pickup"){
            Arm.slide_target.set(12.0)
            Arm.gear_target.set(140.0)
            //arm.intake_servos(-1.0)
            delay(500)
            "_"
        }
        listener.addListener("final_lift_down"){
            Arm.slide_target.set(10.0)
            Arm.gear_target.set(130.0)
            arm.wrist_servos(-140.0, 0.0)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks < 115.0){
                delay(50)
            }
            "final_pickup"
        }
        listener.addListener("final_pickup"){
            Arm.gear_target.set(135.0)
            //arm.intake_servos(-1.0)
            delay(700)
            "finished_final_pickup"
        }
        listener.addListener("park") {
            Arm.slide_target.set(3.0)
            "help"
        }
        listener.addListener("start") {
            while(instance.opModeIsActive() || instance.opModeInInit()){
                arm.go_to_target()
            }
            "started"
        }
    }
}