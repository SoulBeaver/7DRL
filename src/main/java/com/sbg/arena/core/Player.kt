package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import kotlin.properties.Delegates
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Input
import java.util.ArrayList

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
}