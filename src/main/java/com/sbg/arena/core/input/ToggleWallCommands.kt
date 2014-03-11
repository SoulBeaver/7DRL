package com.sbg.arena.core.input

import com.sbg.arena.core.Level
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.InputType

fun toggleWallUp(level: Level, inputType: InputType) {
    if (inputType.isDown())
        return

    val playerCoordinates = level.playerCoordinates

    level.toggleFloor(playerCoordinates.let { Point(it.x, it.y - 1) })
}

fun toggleWallDown(level: Level, inputType: InputType) {
    if (inputType.isDown())
        return

    val playerCoordinates = level.playerCoordinates

    level.toggleFloor(playerCoordinates.let { Point(it.x, it.y + 1) })
}

fun toggleWallLeft(level: Level, inputType: InputType) {
    if (inputType.isDown())
        return

    val playerCoordinates = level.playerCoordinates

    level.toggleFloor(playerCoordinates.let { Point(it.x - 1, it.y) })
}

fun toggleWallRight(level: Level, inputType: InputType) {
    if (inputType.isDown())
        return

    val playerCoordinates = level.playerCoordinates

    level.toggleFloor(playerCoordinates.let { Point(it.x + 1, it.y) })
}