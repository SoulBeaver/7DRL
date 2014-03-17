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

        targets = hashMapOf(Direction.North to shoot(start, Direction.North, { Point(it.x, it.y - 1) }),
                            Direction.East  to shoot(start, Direction.East,  { Point(it.x + 1, it.y) }),
                            Direction.South to shoot(start, Direction.South, { Point(it.x, it.y + 1) }),
                            Direction.West  to shoot(start, Direction.West,  { Point(it.x - 1, it.y) }))
        enemiesHit = targets.values() filter { level[it].isEnemy() } map { enemies[it] }
    }

    override fun isValid() = true

    override fun execute() {
        enemiesHit.forEach { it.takeDamage(10) }
    }

    private fun shoot(point: Point, direction: Direction, increment: (Point) -> Point): Point {
        return if (!level[point].isObstacle()) {
            val next = increment(point)

            if (level.isWithinBounds(next))
                shoot(next, direction, increment)
            else
                point
        } else point
    }
}