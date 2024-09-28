package ca.helios5009.hyperion.core

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap

class MotorWrapper(name: String,hardware: HardwareMap) {
    val motor = hardware.get(DcMotorEx::class.java,name)
    private var power = 0.0
    fun setPower(pow: Double) {
        if(pow != power) {
            motor.power = pow
            power = pow
        }
    }

    fun stop () {
        motor.power = 0.0
        power = 0.0
    }
}