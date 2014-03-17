package com.sbg.arena.core.level

enum class FloorType {
    Floor
    Wall
    Player
    Enemy

    fun isFloor(): Boolean  = (this == FloorType.Floor)
    fun isWall(): Boolean   = (this == FloorType.Wall)
    fun isPlayer(): Boolean = (this == FloorType.Player)
    fun isEnemy(): Boolean  = (this == FloorType.Enemy)
    fun isObstacle(): Boolean = (this == FloorType.Wall || this == FloorType.Enemy)
}

