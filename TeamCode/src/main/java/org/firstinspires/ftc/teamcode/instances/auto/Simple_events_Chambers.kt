package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm

class Simple_events_Chambers (instance:LinearOpMode) {
    val listener = EventListener()
    val arm = Arm(instance)
    val stopper = instance.hardwareMap.get(Servo::class.java, "stopper")

    init {
        arm.init_motors()
        listener.addListener("init") {
            Arm.gear_target.set(30.0)
            stopper.position = 0.5
            Arm.grav.set(false)
            arm.wrist_servos(0.0,0.0)
            for(i in 1..9) {
                arm.go_to_target()
                delay(50)
            }
            Arm.grav.set(true)
            instance.telemetry.addData("Not Yet 0", Arm.grav.get())
            instance.telemetry.update()
            "initialized"
        }
        listener.addListener("start_sample") {
            arm.wrist_servos(0.2,0.2)
            while(instance.opModeInInit() || instance.opModeIsActive()){
                arm.go_to_target(gear_is_on = !Arm.grav.get())
            }
            "started"
        }
        listener.addListener("set_gear") {
            Arm.grav.set(false)
            Arm.gear_target.set(46.0)
            Arm.slide_target.set(5.0)
            delay(1200)
            //arm.wrist_servos(0.45, 0.45)
            //while(arm.gear.getPosition() / arm.gear_degrees_ticks < 32.0){
            //    delay(50)
            //}
            "gear_set"
        }

        listener.addListener("first_chamber") {
            arm.wrist_servos(0.4,0.4)
            //arm.intake_servos(-0.2)
            Arm.slide_target.set(5.3)
            Arm.gear_target.set(62.0)
            delay(1000)
            Arm.grav.set(true)
            //arm.intake_servos(0.0)
            "first_chamber_ready"
        }

        listener.addListener("chamber_up") {
            arm.wrist_servos(0.4,0.4)
            //arm.intake_servos(-0.2)
            Arm.slide_target.set(7.3)
            Arm.gear_target.set(65.0)
            delay(1200)
            Arm.grav.set(true)
            //arm.intake_servos(0.0)
            "chamber_ready"
        }

        listener.addListener("first_score_chamber") {
            Arm.slide_target.set(Arm.slide_target.get() -1.5)
            arm.wrist_servos(0.5,0.5)
            delay(700)
            Arm.slide_target.set(0.4)
            delay(1200)
            /*while(arm.slide.getPosition() / arm.slide_inches_ticks > 4.0) {
                delay(50)
            }*/
            Arm.grav.set(false)
            Arm.slide_target.set(1.0)
            arm.intake_servos(-1.0)
            delay(600)
            arm.intake_servos(0.0)
            "first_score"
        }

        listener.addListener("score_chamber") {
            arm.intake_servos(-0.2)
            Arm.slide_target.set(Arm.slide_target.get() - 5.0)
            delay(800)
            /*while(arm.slide.getPosition() / arm.slide_inches_ticks > 2.0) {
                delay(50)
            }*/
            arm.intake_servos(0.8)
            "score"
        }

        listener.addListener("arm_off") {
            Arm.slide_target.set(4.0)
            delay(1000)
            arm.intake_servos(0.2)
            delay(800)
            arm.intake_servos(0.0)
            "disarmed"
        }

        listener.addListener("ready_arm") {
            arm.wrist_servos(0.15, 0.15)
            Arm.grav.set(false)
            Arm.gear_target.set(60.0)
            delay(1200)
            Arm.gear_target.set(75.0)
            Arm.slide_target.set(2.0)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks < 65.0) {
                delay(50)
            }
            "arm_hover"
        }

        listener.addListener("drop_arm") {
            arm.wrist_servos(0.2,0.2)
            Arm.slide_target.set(4.0)
            Arm.grav.set(true)
            arm.intake_servos(1.0)
            delay(700)
            Arm.slide_target.set(8.0)
            arm.wrist_servos(0.2, 0.2)
            delay(800)
            Arm.slide_target.set(9.3)
            arm.intake_servos(0.8)
            delay(400)
            Arm.grav.set(false)
            Arm.gear_target.set(50.0)
            Arm.slide_target.set(5.0)
            delay(1000)
            Arm.slide_target.set(6.5)
            arm.intake_servos(0.7)
            delay(200)
            "picked_up"
        }

        listener.addListener("_drop_arm") {
            arm.intake_servos(0.8)
            Arm.grav.set(true)
            delay(1500)
            arm.intake_servos(0.0)
            Arm.grav.set(false)
            delay(200)
            "picked_up"
        }

        listener.addListener("arm_up") {
            arm.intake_servos(0.0)
            arm.wrist_servos(0.45,0.45)
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