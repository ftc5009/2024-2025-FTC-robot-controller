package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm

class Simple_events_Chambers (instance:LinearOpMode) {
    val listener = EventListener()
    val arm = Arm(instance)

    init {
        arm.init_motors()
        listener.addListener("start_sample") {
            arm.wrist_servos(0.75,0.75)
            var grav = false
            while(instance.opModeIsActive() || instance.opModeInInit()){
                if(!Arm.grav.get()) {
                    if(!grav) {
                        arm.gear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT)
                        grav = true
                    }
                    arm.go_to_target()
                }else {
                    arm.gear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT)
                    arm.gear.setPower(0.0)
                }
            }
            "started"
        }
        listener.addListener("set_gear") {
            Arm.gear_target.set(40.0)
            delay(1000)
            arm.wrist_servos(0.45, 0.45)
            Arm.slide_target.set(5.0)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks < 35.0){
                delay(50)
            }
            "gear_set"
        }
        listener.addListener("arm_up") {
            Arm.slide_target.set(7.0)
            /*while(arm.slide.getPosition() / arm.slide_inches_ticks < 4.0){
                delay(50)
            }*/
            delay(1000)
            "up_arm"
        }
        listener.addListener("drop") {
            Arm.gear_target.set(55.0)
            delay(1000)
            //arm.intake_servos(-1.0)
            //delay(1000)
            "dropping"
        }
        listener.addListener("drop_done") {
            Arm.gear_target.set(57.0)
            delay(1000)
            Arm.slide_target.set(4.0)
            arm.intake_servos(-0.3)
            Arm.grav.set(true)
            delay(2500)
            arm.intake_servos(-0.8)
            Arm.gear_target.set(47.0)
            arm.intake_servos(0.0)
            Arm.grav.set(false)
            "ahh"
        }
        listener.addListener("lift_down") {
            Arm.gear_target.set(40.0)
            Arm.slide_target.set(3.0)
            "_"
        }
        listener.addListener("ascend") {
            Arm.gear_target.set(55.0)
            "_"
        }
    }
}