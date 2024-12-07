package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm

class Simple_events (instance:LinearOpMode) {
    val listener = EventListener()
    val arm = Arm(instance)

    init {
        arm.init_motors()
        listener.addListener("start_sample") {
            arm.wrist_servos(0.95,0.95)
            while(instance.opModeIsActive() || instance.opModeInInit()){
                arm.go_to_target()
            }
            "started"
        }
        listener.addListener("set_gear") {
            Arm.gear_target.set(40.0)
            delay(1000)
            arm.wrist_servos(0.85, 0.85)
            Arm.slide_target.set(5.0)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks < 35.0){
                delay(50)
            }
            "gear_set"
        }
        listener.addListener("arm_up") {
            Arm.slide_target.set(27.0)
            while(arm.slide.getPosition() / arm.slide_inches_ticks < 24.0){
                delay(50)
            }
            "up_arm"
        }
        listener.addListener("drop") {
            Arm.gear_target.set(22.0)
            delay(3700)
            arm.intake_servos(-1.0)
            delay(1000)
            "dropping"
        }
        listener.addListener("drop_done") {
            Arm.gear_target.set(35.0)
            arm.intake_servos(0.0)
            "_"
        }
        listener.addListener("lift_down") {
            Arm.slide_target.set(8.0)
            "_"
        }
        listener.addListener("ascend") {
            Arm.gear_target.set(55.0)
            "_"
        }
    }
}