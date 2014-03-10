package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import kotlin.properties.Delegates
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Input

enum class Direction {
    NORTH
    EAST
    SOUTH
    WEST
}

class Player(configuration: Configuration) {
    var maxHitPoints: Int
    var hitPoints: Int
    var attack: Int

    {
        maxHitPoints = configuration.hitPoints
        hitPoints = configuration.hitPoints
        attack = configuration.attack
    }

    fun takeDamage(amount: Int) {
        hitPoints = if (hitPoints - amount < 0) 0 else (hitPoints - amount)
    }

    fun heal(amount: Int) {
        hitPoints += if (hitPoints + amount > maxHitPoints) maxHitPoints else (hitPoints + amount)
    }

    fun move(gc: GameContainer): Direction? {
        val input = gc.getInput()!!

        return when {
            input.isKeyDown(Input.KEY_W) -> Direction.NORTH
            input.isKeyDown(Input.KEY_A) -> Direction.WEST
            input.isKeyDown(Input.KEY_S) -> Direction.SOUTH
            input.isKeyDown(Input.KEY_D) -> Direction.EAST
            else                         -> null
        }
    }
}