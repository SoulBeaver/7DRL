package com.sbg.arena.core.input

import com.sbg.arena.core.Level
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.InputType
import com.sbg.arena.core.Direction
import com.sbg.arena.core.Player

class ToggleWallRequest(val level: Level, val player: Player, val direction: Direction): InputRequest {
    val target = when (direction) {
        Direction.North -> level.playerCoordinates.let { Point(it.x, it.y - 1) }
        Direction.South -> level.playerCoordinates.let { Point(it.x, it.y + 1) }
        Direction.East  -> level.playerCoordinates.let { Point(it.x + 1, it.y) }
        Direction.West  -> level.playerCoordinates.let { Point(it.x - 1, it.y) }
    }

    override fun isValid() = (level.isWithinBounds(target) && (level[target].isWall() || level[target].isFloor()))
    override fun execute() = level.toggleFloor(target)
}