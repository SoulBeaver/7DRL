package com.sbg.arena.core

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Input
import kotlin.properties.Delegates
import com.sbg.arena.configuration.Configuration
import java.util.ArrayList

class InputController(val configuration: Configuration) {
    /**
     * Poll input controllers for list of entered commands.
     *
     * @param gc Container with current Input state
     * @return list of pressed keys
     */
    fun update(gc: GameContainer): List<UserInput> {
        val inputController = gc.getInput()!!

        val userInputs = KeyMap.values() map {
            if (inputController.isKeyPressed(it))
                UserInput(it, InputType.Pressed)
            else if (inputController.isKeyDown(it))
                UserInput(it, InputType.Down)
            else null
        } filter { it != null } map { it!! }

        return userInputs
    }

    fun isKeyPressed(gc: GameContainer, keyName: String): Boolean {
        val inputController = gc.getInput()!!

        return inputController.isKeyPressed(KeyMap[keyName]!!)
    }

    fun isKeyDown(gc: GameContainer, keyName: String): Boolean {
        val inputController = gc.getInput()!!

        return inputController.isKeyPressed(KeyMap[keyName]!!)
    }
}

enum class InputType {
    Pressed
    Down

    fun isPressed(): Boolean = (this == Pressed)
    fun isDown(): Boolean    = (this == Down)
}

data class UserInput(val keyCode: Int, val inputType: InputType)

val KeyMap = mapOf("1" to Input.KEY_1,
                   "2" to Input.KEY_2,
                   "3" to Input.KEY_3,
                   "4" to Input.KEY_4,
                   "5" to Input.KEY_5,
                   "6" to Input.KEY_6,
                   "7" to Input.KEY_7,
                   "8" to Input.KEY_8,
                   "9" to Input.KEY_9,
                   "0" to Input.KEY_0,
                   "Z" to Input.KEY_Z,
                   "X" to Input.KEY_X,
                   "C" to Input.KEY_C,
                   "V" to Input.KEY_V,
                   "B" to Input.KEY_B,
                   "N" to Input.KEY_N,
                   "M" to Input.KEY_M,
                   "A" to Input.KEY_A,
                   "S" to Input.KEY_S,
                   "D" to Input.KEY_D,
                   "F" to Input.KEY_F,
                   "G" to Input.KEY_G,
                   "H" to Input.KEY_H,
                   "J" to Input.KEY_J,
                   "K" to Input.KEY_K,
                   "L" to Input.KEY_L,
                   "Q" to Input.KEY_Q,
                   "W" to Input.KEY_W,
                   "E" to Input.KEY_E,
                   "R" to Input.KEY_R,
                   "T" to Input.KEY_T,
                   "Y" to Input.KEY_Y,
                   "U" to Input.KEY_U,
                   "I" to Input.KEY_I,
                   "O" to Input.KEY_O,
                   "P" to Input.KEY_P,
                   "Left" to Input.KEY_LEFT,
                   "Right" to Input.KEY_RIGHT,
                   "Up" to Input.KEY_UP,
                   "Down" to Input.KEY_DOWN,
                   "Space" to Input.KEY_SPACE,
                   "Enter" to Input.KEY_ENTER,
                   "LControl" to Input.KEY_LCONTROL,
                   "RControl" to Input.KEY_RCONTROL,
                   "LShift" to Input.KEY_LSHIFT,
                   "RShift" to Input.KEY_RSHIFT,
                   "Tab" to Input.KEY_TAB,
                   "LAlt" to Input.KEY_LALT,
                   "RAlt" to Input.KEY_RALT)