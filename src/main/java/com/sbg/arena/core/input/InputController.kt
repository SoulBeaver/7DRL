package com.sbg.arena.core

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Input
import kotlin.properties.Delegates
import com.sbg.arena.configuration.Configuration

class InputController(val configuration: Configuration) {
    private val validInputs = listOf(KeyMap[configuration.moveUp]!!,
                                     KeyMap[configuration.moveDown]!!,
                                     KeyMap[configuration.moveLeft]!!,
                                     KeyMap[configuration.moveRight]!!,
                                     KeyMap[configuration.toggleWallUp]!!,
                                     KeyMap[configuration.toggleWallDown]!!,
                                     KeyMap[configuration.toggleWallLeft]!!,
                                     KeyMap[configuration.toggleWallRight]!!,
                                     KeyMap[configuration.shoot]!!)

    private var currentState: Input by Delegates.notNull()

    /**
     * Poll input controllers for list of entered commands.
     *
     * @param gc Container with current Input state
     * @return list of pressed keys
     */
    fun update(gc: GameContainer): List<UserInput> {
        currentState = gc.getInput()!!

        val userInputs = validInputs map {
            if (currentState.isKeyPressed(it))
                UserInput(it, InputType.Pressed)
            else if (currentState.isKeyDown(it))
                UserInput(it, InputType.Down)
            else null
        } filter { it != null } map { it!! }

        return userInputs
    }
}

enum class InputType {
    Pressed
    Down

    fun isPressed(): Boolean = this == Pressed
    fun isDown(): Boolean = this == Down
}

data class UserInput(val keyCode: Int, val inputType: InputType)

val KeyMap = mapOf("1" to 2,
        "2" to 3,
        "3" to 4,
        "4" to 5,
        "5" to 6,
        "6" to 7,
        "7" to 8,
        "8" to 9,
        "9" to 10,
        "0" to 11,
        "Z" to 44,
        "X" to 45,
        "C" to 46,
        "V" to 47,
        "B" to 48,
        "N" to 49,
        "M" to 50,
        "A" to 30,
        "S" to 31,
        "D" to 32,
        "F" to 33,
        "G" to 34,
        "H" to 35,
        "J" to 36,
        "K" to 37,
        "L" to 38,
        "Q" to 16,
        "W" to 17,
        "E" to 18,
        "R" to 19,
        "T" to 20,
        "Y" to 21,
        "U" to 22,
        "I" to 23,
        "O" to 24,
        "P" to 25,
        "Left" to 203,
        "Right" to 205,
        "Up" to 200,
        "Down" to 208,
        "Space" to 57)