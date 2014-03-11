package com.sbg.arena.core.input

import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.Level
import com.sbg.arena.core.level.FloorType
import com.sbg.arena.core.level.isFloor

fun moveUp(level: Level) {
    val playerCoordinates = level.playerCoordinates
    tryMovePlayer(level,
                  playerCoordinates,
                  Point(playerCoordinates.x, playerCoordinates.y - 1))
}

fun moveDown(level: Level) {
    val playerCoordinates = level.playerCoordinates
    tryMovePlayer(level,
                  playerCoordinates,
                  Point(playerCoordinates.x, playerCoordinates.y + 1))
}

fun moveLeft(level: Level) {
    val playerCoordinates = level.playerCoordinates
    tryMovePlayer(level,
                  playerCoordinates,
                  Point(playerCoordinates.x - 1, playerCoordinates.y))
}

fun moveRight(level: Level) {
    val playerCoordinates = level.playerCoordinates
    tryMovePlayer(level,
                  playerCoordinates,
                  Point(playerCoordinates.x + 1, playerCoordinates.y))
}

private fun tryMovePlayer(level: Level, playerCoordinates: Point, destination: Point) {
    if (destination.x < 0 || destination.y < 0 ||
        destination.x >= level.width || destination.y >= level.height)
        return

    if (level[destination].isFloor()) {
        level.movePlayer(destination)
    }
}