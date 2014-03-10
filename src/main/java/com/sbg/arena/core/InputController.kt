package com.sbg.arena.core

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Input
import kotlin.properties.Delegates

class InputController {
    private val validInputs = mapOf("W" to Input.KEY_W,
                                    "A" to Input.KEY_A,
                                    "S" to Input.KEY_S,
                                    "D" to Input.KEY_D,
                                    "Up" to Input.KEY_UP,
                                    "Left" to Input.KEY_LEFT,
                                    "Down" to Input.KEY_DOWN,
                                    "Right" to Input.KEY_RIGHT)

    private var currentState: Input by Delegates.notNull()

    /**
     * Poll input controllers for list of entered commands.
     *
     * @param gc Container with current Input state
     * @return list of pressed keys
     */
    fun update(gc: GameContainer): List<Pair<String, Int>> {
        currentState = gc.getInput()!!

        val userInputs = validInputs map {
            val (key, value) = it

            if (currentState.isKeyPressed(value))
                key to value
            else
                null
        } filter { it != null } map { it!! }

        return userInputs
    }
}