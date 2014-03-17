package com.sbg.arena.core.animation

import org.newdawn.slick.Graphics
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.input.ShootRequest
import kotlin.properties.Delegates
import org.newdawn.slick.Image
import com.sbg.arena.core.geom.Point

class ShootAnimation(val request: ShootRequest, val onAnimationFinished: () -> Unit): Animation {
    var shootSkin: Image by Delegates.notNull()

    var start: Point by Delegates.notNull()
    var targets: List<Point> by Delegates.notNull()

    var bullets: Array<Point> by Delegates.notNull()

    override fun initialize(levelSkin: Skin) {
        shootSkin = levelSkin.shootTile()

        start = request.start
        targets = request.targets

        bullets = Array<Point>(4, { start })
    }

    override fun update() {
        bullets[0] = bullets[0].let { Point(it.x, it.y - 1) }
        bullets[0] = bullets[0].let { Point(it.x + 1, it.y) }
        bullets[0] = bullets[0].let { Point(it.x , it.y + 1) }
        bullets[0] = bullets[0].let { Point(it.x + 1, it.y) }
    }

    override fun render(graphics: Graphics) {
        bullets.forEach {
            shootSkin.draw(it.x.toFloat(),
                           it.y.toFloat())
        }
    }

    override fun isFinished(): Boolean {
        return bullets.toList() == targets
    }

    override fun finish() {
        onAnimationFinished()
    }
}