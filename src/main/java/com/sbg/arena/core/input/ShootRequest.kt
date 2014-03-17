package com.sbg.arena.core.input

import com.sbg.arena.core.Level
import com.sbg.arena.core.Player
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.enemy.Enemies
import com.sbg.arena.core.Direction
import kotlin.properties.Delegates
import com.sbg.arena.core.enemy.Enemy

class ShootRequest(val level: Level,
                   val player: Player,
                   val enemies: Enemies): InputRequest {
    var start: Point by Delegates.notNull()
    var targets: Map<Direction, Point> by Delegates.notNull()
    var enemiesHit: List<Enemy> by Delegates.notNull()

    override fun initialize() {
        start = level.playerCoordinates

        targets = hashMapOf(Direction.North to shoot(start, Direction.North),
                            Direction.East  to shoot(start, Direction.East),
                            Direction.South to shoot(start, Direction.South),
                            Direction.West  to shoot(start, Direction.West))
        enemiesHit = targets.values() filter { level[it].isEnemy() } map { enemies[it] }
    }

    override fun isValid() = true

    override fun execute() {
        enemiesHit.forEach { it.takeDamage(10) }
    }

    private fun shoot(at: Point, direction: Direction): Point {
        return when (direction) {
            Direction.North -> {
                if (!level[at].isObstacle()) {
                    val next = at.let { Point(it.x, it.y - 1) }

                    if (level.isWithinBounds(next))
                        shoot(next, Direction.North)
                    else

                        at
                } else at
            }

            Direction.East -> {
                if (!level[at].isObstacle()) {
                    val next = at.let { Point(it.x + 1, it.y) }

                    if (level.isWithinBounds(next))
                        shoot(next, Direction.East)
                    else
                        at
                } else at
            }

            Direction.South -> {
                if (!level[at].isObstacle()) {
                    val next = at.let { Point(it.x, it.y + 1) }

                    if (level.isWithinBounds(next))
                        shoot(next, Direction.South)
                    else
                        at
                } else at
            }
            Direction.West -> {
                if (!level[at].isObstacle()) {
                    val next = at.let { Point(it.x - 1, it.y) }

                    if (level.isWithinBounds(next))
                        shoot(next, Direction.West)
                    else
                        at
                }
                else at
            }
        }
    }
}