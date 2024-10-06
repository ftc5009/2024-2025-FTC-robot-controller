package org.firstinspires.ftc.teamcode.instances.auto

import ca.helios5009.hyperion.core.HyperionPath
import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.core.Movement
import ca.helios5009.hyperion.misc.PositionTracking
import ca.helios5009.hyperion.misc.PositionType
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.DriveConstants
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.RotateConstants
import org.firstinspires.ftc.teamcode.StrafeConstants
import kotlin.collections.ArrayList

class MainAuto(private val instance : LinearOpMode) {
    val bot = Robot(instance)
    val motors = Motors(bot.fl, bot.fr, bot.br, bot.bl)
    val eventListner = EventListener()
    val movement = Movement(instance, eventListner, motors, PositionTracking.OTOS)


    init {
        movement.setTracking(bot.otos, null)
        movement.setControllerConstants(
            doubleArrayOf(DriveConstants.GainSpeed, DriveConstants.AccelerationLimit, DriveConstants.DefaultOutputLimit, DriveConstants.Tolerance, DriveConstants.Deadband),
            doubleArrayOf(StrafeConstants.GainSpeed,StrafeConstants.AccelerationLimit, StrafeConstants.DefaultOutputLimit, StrafeConstants.Tolerance, StrafeConstants.Deadband),
            doubleArrayOf(RotateConstants.GainSpeed, RotateConstants.AccelerationLimit, RotateConstants.DefaultOutputLimit, RotateConstants.Tolerance, RotateConstants.Deadband),
        )
    }

}