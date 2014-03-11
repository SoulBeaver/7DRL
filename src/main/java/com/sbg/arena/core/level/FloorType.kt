package com.sbg.arena.core.level

enum class FloorType {
    Floor
    Wall
    Player

    fun isFloor(): Boolean  = this == FloorType.Floor
    fun isWall(): Boolean   = this == FloorType.Wall
    fun isPlayer(): Boolean = this == FloorType.Player
}

