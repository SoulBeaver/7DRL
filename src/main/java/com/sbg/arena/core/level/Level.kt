package com.sbg.arena.core

import com.google.common.base.Preconditions
import org.apache.logging.log4j.LogManager
import kotlin.properties.Delegates
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.level.FloorType
import com.sbg.arena.util.toAsciiString
import com.sbg.arena.util.withIndices
import java.util.ArrayList
import com.sbg.arena.core.level.isFloor
import com.sbg.arena.core.level.isWall

class Level(val dimension: Dimension,
            private val level: Array<FloorType>) {
    private val logger = LogManager.getLogger(javaClass<Level>())

    val width  = dimension.width
    val height = dimension.height
    val area   = width * height

    var playerCoordinates: Point by Delegates.notNull()
    fun placePlayer() {
        for ((coordinates, floorType) in withIndices()) {
            if (floorType.isFloor()) {
                set(coordinates, FloorType.Player)
                playerCoordinates = coordinates
                return
            }
        }

        throw IllegalStateException("Unable to place player, please check the map")
    }

    fun movePlayer(destination: Point) {
        if (!isWithinBounds(destination) || get(destination).isWall())
            return

        set(playerCoordinates, FloorType.Floor)
        set(destination, FloorType.Player)
        playerCoordinates = destination
    }

    fun toggleFloor(target: Point) {
        if (!isWithinBounds(target))
            return

        val floorType = get(target)

        if (floorType.isFloor())
            set(target, FloorType.Wall)
        else if (floorType.isWall())
            set(target, FloorType.Floor)
    }

    fun isWithinBounds(point: Point): Boolean {
        val (x, y) = point

        return (x >= 0 && x < width && y >= 0 && y <= height)
    }

    fun get(index: Int): FloorType = level[index]
    fun get(point: Point): FloorType = get(point.x, point.y)
    fun get(x: Int, y: Int): FloorType = level[x + y * width]

    fun set(point: Point, floorType: FloorType) {
        set(point.x, point.y, floorType)
    }
    fun set(x: Int, y: Int, floorType: FloorType) {
        level[x + y * width] = floorType
    }

    fun toString():String {
        return toAsciiString()
    }

    assert {
        Preconditions.checkArgument(area == level.size,
                                    "Level does not conform to the given dimensions; expected ${area}, but was ${level.size}")
    }
}