package com.sbg.arena.core.level

enum class FloorType {
    Floor
    Wall
    Player
}

fun FloorType.isFloor(): Boolean {
    return this == FloorType.Floor
}

fun FloorType.isWall(): Boolean {
    return this == FloorType.Wall
}

fun FloorType.isPlayer(): Boolean {
    return this == FloorType.Player
}