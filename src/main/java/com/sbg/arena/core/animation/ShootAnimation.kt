package com.sbg.arena.core.animation

import org.newdawn.slick.Graphics
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.input.ShootRequest
import kotlin.properties.Delegates
import org.newdawn.slick.Image
import com.sbg.arena.core.geom.Point
import org.apache.logging.log4j.LogManager
import com.sbg.arena.core.Direction

class ShootAnimation(val request: ShootRequest, val onAnimationFinished: () -> Unit): Animation {
    private val logger = LogManager.getLogger(javaClass<ShootAnimation>())!!

    var shootSkin: Image by Delegates.notNull()

    var start: Point by Delegates.notNull()
    var targets: Map<Direction, Point> by Delegates.notNull()
    var shots: MutableMap<Direction, Point> by Delegates.notNull()

    override fun initialize(levelSkin: Skin) {
        shootSkin = levelSkin.shootTile()

        start = request.start.let { Point(it.x * 20, it.y * 20) }
        targets = hashMapOf(Direction.North to request.targets[Direction.North]!!.let { Point(it.x * 20, it.y * 20) },
                            Direction.East  to request.targets[Direction.East]!!.let  { Point(it.x * 20, it.y * 20) },
                            Direction.South to request.targets[Direction.South]!!.let { Point(it.x * 20, it.y * 20) },
                            Direction.West  to request.targets[Direction.West]!!.let  { Point(it.x * 20, it.y * 20) })

        shots = hashMapOf(Direction.North to start,
                          Direction.East  to start,
                          Direction.South to start,
                          Direction.West  to start)
    }

    override fun update() {
        shots.entrySet().filter { it.getValue() == targets[it.getKey()] } forEach { shots.remove(it.getKey()) }

        for ((direction, shot) in shots.entrySet())
            shots[direction] = update(direction, shot)
    }

    private fun update(direction: Direction, shot: Point): Point {
        return when (direction) {
            Direction.North -> Point(shot.x, shot.y - 5)
            Direction.South -> Point(shot.x, shot.y + 5)
            Direction.West  -> Point(shot.x - 5, shot.y)
            Direction.East  -> Point(shot.x + 5, shot.y)
        }
    }

    override fun render(graphics: Graphics) {
        shots.values().forEach { shootSkin.draw(it.x.toFloat(), it.y.toFloat()) }
    }

    override fun isFinished() = shots.isEmpty()
    override fun finish() = onAnimationFinished()
}