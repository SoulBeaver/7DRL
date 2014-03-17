package com.sbg.arena.core.animation

import org.newdawn.slick.Graphics
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.input.ShootRequest
import kotlin.properties.Delegates
import org.newdawn.slick.Image
import com.sbg.arena.core.geom.Point

class ShootAnimation(val request: ShootRequest): Animation {
    var shootSkin: Image by Delegates.notNull()

    var current: Point by Delegates.notNull()
    var end: Point by Delegates.notNull()

    override fun initialize(levelSkin: Skin) {
       shootSkin = levelSkin.shootTile()


    }

    override fun update() {

    }

    override fun render(graphics: Graphics) {

    }

    override fun isFinished(): Boolean {
        return true
    }

    override fun finish() {

    }
}