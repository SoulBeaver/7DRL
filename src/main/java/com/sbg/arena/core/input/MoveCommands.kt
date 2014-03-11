package com.sbg.arena.core.input

import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.Level
import com.sbg.arena.core.level.FloorType
import com.sbg.arena.core.level.isFloor

fun moveUp(level: Level) {
    val playerCoordinates = level.playerCoordinates

    level.movePlayer(playerCoordinates.let { Point(it.x, it.y - 1) })
}

fun moveDown(level: Level) {
    val playerCoordinates = level.playerCoordinates

    level.movePlayer(playerCoordinates.let { Point(it.x, it.y + 1) })
}

fun moveLeft(level: Level) {
    val playerCoordinates = level.playerCoordinates

    level.movePlayer(playerCoordinates.let { Point(it.x - 1, it.y) })
}

fun moveRight(level: Level) {
    val playerCoordinates = level.playerCoordinates

    level.movePlayer(playerCoordinates.let { Point(it.x + 1, it.y) })
}