package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.components.Arm

class Events (instance:LinearOpMode){
    val listener = EventListener()
    val arm = Arm(instance)
    init {
        arm.init_motors()
        listener.addListener("lift") {
            Arm.slide_target.set(24.0)
            Arm.gear_target.set(20.0)
            //arm.wrist_servos(90.0, 25.0)
            delay(1500)
            "drop"
        }
        listener.addListener("drop") {
            //arm.intake_servos(1.0)
            delay(500)
            //arm.intake_servos(0.0)
            "finish_dropping"
        }
        listener.addListener("lift_down") {
            Arm.slide_target.set(9.5)
            Arm.gear_target.set(115.0)
            arm.wrist_servos(50.0, 90.0)
            delay(1000)
            "pickup"
        }
        listener.addListener("pickup"){
            Arm.gear_target.set(125.0)
            //arm.intake_servos(-1.0)
            delay(300)
            //arm.intake_servos(0.0)
            "finish_pickup"
        }
        listener.addListener("park") {
            Arm.slide_target.set(3.0)
            "help"
        }
    }
}