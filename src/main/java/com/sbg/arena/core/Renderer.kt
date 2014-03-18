package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import org.apache.logging.log4j.LogManager
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.Graphics
import com.sbg.arena.core.Player
import com.sbg.arena.core.Level
import com.sbg.arena.core.Camera
import kotlin.properties.Delegates
import org.newdawn.slick.Color
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.geom.Rectangle
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.input.InputRequest
import com.sbg.arena.core.input.MoveRequest
import com.sbg.arena.core.animation.MoveAnimation
import java.util.ArrayList
import com.sbg.arena.core.animation.Animation
import com.sbg.arena.core.animation.ToggleWallAnimation
import com.sbg.arena.core.input.ToggleWallRequest
import com.sbg.arena.core.animation.ShootAnimation
import com.sbg.arena.core.input.ShootRequest

class Renderer(val configuration: Configuration,
               val level: Level,
               val levelSkin: Skin) {
    private val logger = LogManager.getLogger(javaClass<Renderer>())!!

    private var camera = Camera(configuration)
    private var animationPlayer = AnimationPlayer(levelSkin)

    fun update() {
        if (animationPlayer.isPlaying())
            animationPlayer.update()

        camera.update(level.playerCoordinates)
    }

    fun render(graphics: Graphics) {
        graphics.setBackground(Color.white)

        val viewArea = calculateViewArea()

        camera.renderGameplay(graphics) {
            levelSkin.render(level, viewArea)

            animationPlayer.render(graphics)
        }
    }

    private fun calculateViewArea(): Rectangle {
        // Many thanks to koiwai for this much improved calculation!
        // Source: http://forums.roguetemple.com/index.php?topic=3933.15
        val (playerX, playerY) = level.playerCoordinates
        val distance = 15 // TODO: Hardcoded distance.

        val centerX = Math.min(Math.max(playerX, distance), level.width  - distance - 1)
        val centerY = Math.min(Math.max(playerY, distance), level.height - distance - 1)

        val start = Point(centerX - distance,
                          centerY - distance)
        val end = Point(start.x + 2 * distance + 1,
                        start.y + 2 * distance + 1)

        return Rectangle(start, end)
    }

    fun play(animation: Animation) =
        animationPlayer.play(animation)

    fun onAllAnimationsFinished(action: () -> Unit) =
        animationPlayer.onAllAnimationsFinished(action)

    fun hasAnimationsPlaying() =
        animationPlayer.isPlaying()
}