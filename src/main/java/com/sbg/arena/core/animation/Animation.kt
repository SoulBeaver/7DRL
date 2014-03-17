package com.sbg.arena.core.animation

import org.newdawn.slick.Graphics
import com.sbg.arena.core.level.Skin

trait Animation {
    fun initialize(levelSkin: Skin)
    fun update()
    fun render(graphics: Graphics)
    fun isFinished(): Boolean
    fun finish()
}
