package com.sbg.arena.core.enemy

import com.sbg.arena.core.geom.Point
import java.util.HashMap
import com.google.common.base.Preconditions

class Enemies {
    private val enemies = HashMap<Point, Enemy>()

    fun add(enemy: Enemy, at: Point) {
        enemies.put(at, enemy)
    }

    fun remove(x: Int, y: Int) {
        remove(Point(x, y))
    }

    fun remove(at: Point) {
        Preconditions.checkArgument(enemies.containsKey(at),
                                    "No enemy exists at $at")

        enemies.remove(at)
    }

    fun remove(enemy: Enemy) {
        val candidate = enemies.entrySet().find { it.getValue() == enemy }
        Preconditions.checkArgument(candidate != null,
                                    "$enemy does not exist!")

        enemies.remove(candidate!!.getKey())
    }

    fun get(at: Point): Enemy {
        return enemies.get(at)!!
    }

    fun get(x: Int, y: Int): Enemy {
        return get(Point(x, y))
    }
}