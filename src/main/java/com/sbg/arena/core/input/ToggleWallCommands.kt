package com.sbg.arena.core.input

import com.sbg.arena.core.Level
import com.sbg.arena.core.geom.Point

fun toggleWallUp(level: Level) {
    val playerCoordinates = level.playerCoordinates

    level.toggleFloor(playerCoordinates.let { Point(it.x, it.y - 1) })
}

fun toggleWallDown(level: Level) {
    val playerCoordinates = level.playerCoordinates

    level.toggleFloor(playerCoordinates.let { Point(it.x, it.y + 1) })
}

fun toggleWallLeft(level: Level) {
    val playerCoordinates = level.playerCoordinates

    level.toggleFloor(playerCoordinates.let { Point(it.x - 1, it.y) })
}

fun toggleWallRight(level: Level) {
    val playerCoordinates = level.playerCoordinates

    level.toggleFloor(playerCoordinates.let { Point(it.x + 1, it.y) })
}