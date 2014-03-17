package com.sbg.arena.core.input

import com.sbg.arena.core.Level
import com.sbg.arena.core.Player
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.enemy.Enemies
import com.sbg.arena.core.Direction

class ShootRequest(val level: Level,
                   val player: Player,
                   val enemies: Enemies): InputRequest {
    override fun isValid() = true
    override fun execute() {
        val start = level.playerCoordinates

        val targets = array(Direction.North,
                            Direction.East,
                            Direction.South,
                            Direction.West) map { shoot(start, it) } filterNot { level[it].isWall() }

        targets.forEach { enemies[it].takeDamage(10) }
    }

    private fun shoot(at: Point, direction: Direction): Point {
        return when (direction) {
            Direction.North -> if (level[at].isObstacle()) shoot(Point(at.x, at.y - 1), Direction.North) else at
            Direction.East  -> if (level[at].isObstacle()) shoot(Point(at.x - 1, at.y), Direction.North) else at
            Direction.South -> if (level[at].isObstacle()) shoot(Point(at.x, at.y + 1), Direction.North) else at
            Direction.West  -> if (level[at].isObstacle()) shoot(Point(at.x + 1, at.y), Direction.North) else at
        }
    }
}