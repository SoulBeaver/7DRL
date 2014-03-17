package com.sbg.arena.core.animation

import org.newdawn.slick.Graphics
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.input.ShootRequest
import kotlin.properties.Delegates
import org.newdawn.slick.Image
import com.sbg.arena.core.geom.Point
import org.apache.logging.log4j.LogManager

class ShootAnimation(val request: ShootRequest, val onAnimationFinished: () -> Unit): Animation {
    private val logger = LogManager.getLogger(javaClass<ShootAnimation>())!!

    var shootSkin: Image by Delegates.notNull()

    var start: Point by Delegates.notNull()
    var targets: List<Point> by Delegates.notNull()

    var bullets: Array<Point> by Delegates.notNull()

    override fun initialize(levelSkin: Skin) {
        shootSkin = levelSkin.shootTile()

        start = request.start.let { Point(it.x * 20, it.y * 20) }
        targets = request.targets.map { Point(it.x * 20, it.y * 20) }

        bullets = Array<Point>(4, { start })

        logger.debug("Starting point:  $start")
        logger.debug("Targets:  ${targets[0]}, ${targets[1]}, ${targets[2]}, ${targets[3]}")
        logger.debug("Bullet starting points:  ${bullets[0]}, ${bullets[1]}, ${bullets[2]}, ${bullets[3]}")
    }

    override fun update() {
        if (bullets[0] != targets[0])
            bullets[0] = bullets[0].let { Point(it.x, it.y - 5) }

        if (bullets[1] != targets[1])
            bullets[1] = bullets[1].let { Point(it.x + 5, it.y) }

        if (bullets[2] != targets[2])
            bullets[2] = bullets[2].let { Point(it.x , it.y + 5) }

        if (bullets[3] != targets[3])
            bullets[3] = bullets[3].let { Point(it.x - 5, it.y) }
    }

    override fun render(graphics: Graphics) {
        for ((i, bullet) in bullets.withIndices()) {
            if (bullets[i] != targets[i])
                shootSkin.draw(bullet.x.toFloat(), bullet.y.toFloat())
        }
    }

    override fun isFinished(): Boolean {
        return bullets.toList() == targets
    }

    override fun finish() {
        onAnimationFinished()
    }
}