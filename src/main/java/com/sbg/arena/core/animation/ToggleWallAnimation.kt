package com.sbg.arena.core.animation

import com.sbg.arena.core.input.ToggleWallRequest
import com.sbg.arena.core.level.Skin
import org.newdawn.slick.Graphics
import kotlin.properties.Delegates
import org.newdawn.slick.Image
import com.sbg.arena.core.level.FloorType
import org.newdawn.slick.Color

class ToggleWallAnimation(val request: ToggleWallRequest): Animation {
    private var floorSkin: Image by Delegates.notNull()
    private var wallSkin: Image by Delegates.notNull()

    private var current = 1F

    override fun initialize(levelSkin: Skin) {
        floorSkin = levelSkin.floorTile()
        wallSkin  = levelSkin.wallTile()

        request.level.toggleFloor(request.target)
    }

    override fun update() {
        current -= 0.02F
    }

    override fun render(graphics: Graphics) {
        when (request.level[request.target]) {
            FloorType.Floor -> wallSkin.draw(request.target.x.toFloat() * 20,
                                             request.target.y.toFloat() * 20,
                                             Color(1F, 1F, 1F, current))
            FloorType.Wall  -> floorSkin.draw(request.target.x.toFloat() * 20,
                                              request.target.y.toFloat() * 20,
                                              Color(1F, 1F, 1F, current))
        }
    }

    override fun isFinished(): Boolean {
        return current <= 0F
    }

    override fun finish() {
        request.level.toggleFloor(request.target)
    }
}