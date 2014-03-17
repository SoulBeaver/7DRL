package com.sbg.arena.core.animation

import com.sbg.arena.core.input.InputRequest
import com.sbg.arena.core.input.MoveRequest
import com.sbg.arena.core.level.Skin
import org.newdawn.slick.Image
import com.sbg.arena.core.level.FloorType
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.Direction
import org.newdawn.slick.Graphics
import kotlin.properties.Delegates

trait Animation {
    fun initialize(levelSkin: Skin)
    fun update()
    fun render(graphics: Graphics)
    fun isFinished(): Boolean
}

class MoveAnimation(val moveRequest: MoveRequest): Animation {
    private var playerSkin: Image by Delegates.notNull()

    private val from = moveRequest.level.playerCoordinates.let { Point(it.x * 20, it.y * 20) }
    private val to   = moveRequest.destination.let { Point(it.x * 20, it.y * 20) }

    private var current = from

    override fun initialize(levelSkin: Skin) {
        playerSkin = levelSkin.playerTile()

        val level = moveRequest.level
        level[level.playerCoordinates] = FloorType.Floor
    }

    override fun update() {
        current = when (moveRequest.direction) {
            Direction.North -> current.let { Point(it.x,     it.y - 1) }
            Direction.East  -> current.let { Point(it.x + 1, it.y) }
            Direction.South -> current.let { Point(it.x,     it.y + 1) }
            Direction.West  -> current.let { Point(it.x - 1, it.y) }
        }
    }

    override fun render(graphics: Graphics) {
        playerSkin.draw(current.x.toFloat(), current.y.toFloat())
    }

    override fun isFinished(): Boolean {
        return (current == to)
    }
}