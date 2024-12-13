package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm

class Simple_events (instance:LinearOpMode) {
    val listener = EventListener()
    val arm = Arm(instance)
    val stopper = instance.hardwareMap.get(Servo::class.java, "stopper")

    init {
        arm.init_motors()
        listener.addListener("init") {
            Arm.gear_target.set(30.0)
            stopper.position = 0.0
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
            arm.wrist_servos(0.25,0.25)
            while(instance.opModeIsActive() || instance.opModeInInit()){
                arm.go_to_target(gear_is_on = !Arm.grav.get())
            }
            Arm.grav.set(false)
            "started"
        }
        listener.addListener("set_gear") {
            /*if(arm.gear.getPosition() / arm.gear_degrees_ticks < 29.0) {
                Arm.grav.set(true)

                arm.gear.setPower(1.0)
                delay(500)

            }*/
            Arm.gear_target.set(58.0)
            delay(2000)
            arm.wrist_servos(0.25, 0.25)
            Arm.gear_target.set(40.0)
            Arm.slide_target.set(5.0)
            while(arm.gear.getPosition() / arm.gear_degrees_ticks < 30.0){
                delay(50)
            }
            Arm.grav.set(false)
            Arm.gear_target.set(32.0)
            delay(400)
            "gear_set"
        }
        listener.addListener("arm_up") {
            arm.intake_servos(0.0)
            Arm.gear_target.set(17.0)
            Arm.slide_target.set(12.0)
            delay(900)
            arm.intake_servos(0.0)
            Arm.slide_target.set(27.0)
            delay(500)
            Arm.grav.set(true)
            delay(100)
            Arm.gear_target.set(35.0)
            while(arm.slide.getPosition() / arm.slide_inches_ticks < 21.0){
                delay(50)
            }
            delay(500)
            arm.wrist_servos(0.05, 0.05)
            "up_arm"
        }
        listener.addListener("drop_sample") {
            arm.intake_servos(-1.0)
            delay(1600)
            arm.wrist_servos(0.45, 0.45)
            delay(200)
            arm.intake_servos(0.0)
            Arm.slide_target.set(4.0)
            delay(800)
            "dropped"
        }
        listener.addListener("lift_down") {
            Arm.grav.set(false)
            Arm.gear_target.set(69.0)
            "lift_ready"
        }
        listener.addListener("lift_down_final") {
            Arm.grav.set(false)
            Arm.gear_target.set(36.0)
            Arm.slide_target.set(8.0)
            "_"
        }
        listener.addListener("pick_up") {
            Arm.grav.set(true)
            //arm.wrist_servos(0.45,0.45)
            arm.intake_servos(1.0)
            delay(1200)
            arm.intake_servos(0.8)
            Arm.grav.set(false)
            delay(300)
            arm.intake_servos(0.0)
            "picked_up"
        }
        listener.addListener("ascend") {
            Arm.gear_target.set(55.0)
            delay(1000)
            Arm.grav.set(true)
            "_"
        }
    }
}