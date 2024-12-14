package org.firstinspires.ftc.teamcode.instances.teleop

import android.os.SystemClock
import ca.helios5009.hyperion.hardware.HyperionMotor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.components.Arm

class TeleOp_GamePads (private val instance: LinearOpMode) {

    private val gamepad1 = instance.gamepad1
    private val gamepad2 = instance.gamepad2

    val arm = Arm(instance)
    val hang_arm = HyperionMotor(instance.hardwareMap, "hang_arm")
    val stopper = instance.hardwareMap.get(Servo::class.java, "stopper")


    var armState = Arm.ArmState.ENDED
    val cruise_timer = ElapsedTime()
    var y_pressed = false
    var x_pressed = false
    var a1_pressed = false
    var a2_pressed = false
    var b1_pressed = false
    var b_pressed = false
    var manual_slide = false
    var manual_wrist = false

    fun set_stopper(pos : Double) {
        stopper.position = pos
    }

    fun game_pad_1() {
        if (gamepad1.b && armState == Arm.ArmState.SUBMERSIBLE && !b1_pressed) {
            arm.wrist_servos(0.0, 0.0)
            set_stopper(0.5)
            Arm.grav.set(true)
            armState = Arm.ArmState.WALL_PICKUP
            b1_pressed = true
        } else if (gamepad1.b) {
            arm.wrist_servos(0.3, 0.3)
            set_stopper(0.5)
        } else if (!gamepad1.b && b1_pressed) {
            if (armState == Arm.ArmState.WALL_PICKUP) {
                Arm.grav.set(false)
                armState = Arm.ArmState.SUBMERSIBLE
            }
            b1_pressed = false
        }

        // ARM DOWN
        if (armState == Arm.ArmState.SUBMERSIBLE && gamepad1.a && !gamepad1.start && !a1_pressed) {
            Arm.grav.set(true)
            set_stopper(0.0)
            arm.wrist_servos(0.45, 0.45)
            a1_pressed = true
        } else if (gamepad1.a && !gamepad1.start && !a1_pressed) {
            Arm.grav.set(true)
            set_stopper(0.0)
            a1_pressed = true
        } else if (!gamepad1.a && !gamepad1.start && armState == Arm.ArmState.SUBMERSIBLE && a1_pressed) {
            set_stopper(0.0)
            Arm.grav.set(false)
            a1_pressed = false
        } else if (!gamepad1.a && a1_pressed && !gamepad1.start) {
            a1_pressed = false
            Arm.grav.set(false)
            stopper.position = 0.5
        }
        if(gamepad1.x && armState == Arm.ArmState.SUBMERSIBLE){
            arm.wrist_servos(0.05, 0.05)
            set_stopper(0.5)

        }

        // MANUAL GEAR MOVEMENT
        if (gamepad1.right_bumper && !Arm.grav.get()) {
            Arm.gear_target.set(Range.clip(Arm.gear_target.get() + 0.1, 20.0, 125.0))
        } else if (gamepad1.left_bumper && !Arm.grav.get()) {
            Arm.gear_target.set(Range.clip(Arm.gear_target.get() - 0.1, 20.0, 125.0))
        }

        // MANUAL SLIDE MOVEMENT
        if (Arm.gear_target.get() < 45.0) {
            if (gamepad1.right_trigger > 0.5 && manual_slide) {
                Arm.slide_target.set(Range.clip(Arm.slide_target.get() + 0.1, 0.0, 27.0))
            } else if (gamepad1.left_trigger > 0.5 && manual_slide) {
                Arm.slide_target.set(Range.clip(Arm.slide_target.get() - 0.1, 0.0, 27.0))
            }
        } else {
            if (gamepad1.right_trigger > 0.5 && manual_slide) {
                Arm.slide_target.set(Range.clip(Arm.slide_target.get() + 0.1, 3.0, 9.5))
            } else if (gamepad1.left_trigger > 0.5 && manual_slide) {
                Arm.slide_target.set(Range.clip(Arm.slide_target.get() - 0.1, 3.0, 9.5))
            }
        }
    }

    fun game_pad_2() {
        // NET ZONE
        if (gamepad2.y && !y_pressed) {
            if (armState == Arm.ArmState.LOW_BASKET) {
                //high basket
                Arm.gear_target.set(22.0)
                Arm.slide_target.set(27.0)
                Arm.grav.set(true)
                arm.wrist_servos(0.0, 0.0)
                manual_slide = true
                armState = Arm.ArmState.HIGH_BASKET
            } else if (armState == Arm.ArmState.CRUISE) {
                //low basket
                Arm.gear_target.set(22.0)
                Arm.slide_target.set(8.0)
                Arm.grav.set(true)
                arm.wrist_servos(0.1, 0.1)
                manual_slide = false
                armState = Arm.ArmState.LOW_BASKET
            } else {
                //no slide
                if(armState != Arm.ArmState.HIGH_BASKET) {
                    cruise_timer.reset()
                    Arm.grav.set(false)
                } else {
                    Arm.grav.set(true)
                }
                Arm.gear_target.set(22.0)
                Arm.slide_target.set(3.0)
                armState = Arm.ArmState.CRUISE
            }
            y_pressed = true
        } else if (!gamepad2.y && y_pressed) {
            if (armState == Arm.ArmState.CRUISE && arm.gear.getPosition() / arm.gear_degrees_ticks < 30.0) {
                Arm.free_slide.set(true)
                y_pressed = false
            } else if(armState != Arm.ArmState.CRUISE) {
                Arm.free_slide.set(false)
                y_pressed = false
            }
        }

        // PICK UP
        if (gamepad2.a && !gamepad2.start && !a2_pressed && armState != Arm.ArmState.HIGH_BASKET) {
            //sample pick up from submersible
            Arm.gear_target.set(100.0)
            Arm.slide_target.set(5.0)
            arm.wrist_servos(0.43, 0.43)
            manual_slide = true
            manual_wrist = true
            Arm.free_slide.set(false)
            Arm.grav.set(false)
            armState = Arm.ArmState.SUBMERSIBLE
            a2_pressed = true
            a1_pressed = true
        } else if (!gamepad2.a && a2_pressed) {
            a2_pressed = false
        }

        // CHAMBERS
        if (gamepad2.x && !x_pressed && armState != Arm.ArmState.HIGH_BASKET) {
            //high chamber for specimens
            if (armState == Arm.ArmState.HIGH_CHAMBER) {
                //high chamber scoring
                Arm.gear_target.set(65.0)
                Arm.slide_target.set(1.0)
                armState = Arm.ArmState.HIGH_CHAMBER_SCORE
            } else {
                //high chamber
                Arm.gear_target.set(55.0)
                Arm.slide_target.set(8.5)
                arm.wrist_servos(0.4, 0.4)
                armState = Arm.ArmState.HIGH_CHAMBER
            }
            Arm.free_slide.set(false)
            Arm.grav.set(false)
            x_pressed = true
        } else if (!gamepad2.x && x_pressed) {
            x_pressed = false
        }

        // LOW CHAMBERS
        if (gamepad2.b && !gamepad2.start && !b_pressed && armState != Arm.ArmState.HIGH_BASKET) {
            //high chamber for specimens
            if (armState == Arm.ArmState.LOW_CHAMBER) {
                //low chamber score
                Arm.gear_target.set(100.0)
                Arm.slide_target.set(0.4)
                Arm.ArmState.LOW_CHAMBER_SCORE
            } else {
                //low chamber
                Arm.gear_target.set(95.0)
                Arm.slide_target.set(5.0)
                arm.wrist_servos(0.45, 0.45)
                armState = Arm.ArmState.LOW_CHAMBER
            }
            Arm.free_slide.set(false)
            Arm.grav.set(false)
            b_pressed = true
        } else if (!gamepad2.b && b_pressed) {
            b_pressed = false
        }

        // HANGING
        if(gamepad2.left_stick_y > 0.5) {
            hang_arm.setPower(-1.0)  //lift robot
        } else if(gamepad2.left_stick_y < -0.5) {
            hang_arm.setPower(0.6)   //raise hook
        } else {
            hang_arm.setPower(0.0)
        }

        // INTAKE SERVOS
        if (gamepad2.right_bumper) {
            arm.intake_servos(1.0)  //outake
        } else if (gamepad2.left_bumper) {
            arm.intake_servos(-1.0)
        } else {
            arm.intake_servos(0.0)
        }

        // WRIST MOVEMENT
        /*if (gamepad2.dpad_up && manual_wrist) {
            if (arm.right_wrist.position != 1.0 && arm.left_wrist.position != 1.0 && arm.right_wrist.position + arm.left_wrist.position < 1.0) {
                arm.right_wrist.position += 0.01
                arm.left_wrist.position += 0.01

            } else if (gamepad2.dpad_down && manual_wrist) {
                if (arm.right_wrist.position != 0.0 && arm.left_wrist.position != 0.0 && arm.right_wrist.position + arm.left_wrist.position > 0.0) {
                    arm.right_wrist.position -= 0.01
                    arm.left_wrist.position -= 0.01
                }
            }
        }*/
        /*if (gamepad2.dpad_left && manual_wrist) {
            if(arm.right_wrist.position != 1.0 && arm.left_wrist.position != 0.0) {
                arm.right_wrist.position += 0.01
                arm.left_wrist.position -= 0.01
            }
        } else if (gamepad2.dpad_right && manual_wrist) {
            if(arm.right_wrist.position != 0.0 && arm.left_wrist.position != 1.0) {
                arm.right_wrist.position -= 0.01
                arm.left_wrist.position += 0.01
            }
        }*/
    }

}