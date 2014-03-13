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
import com.google.common.eventbus.EventBus
import com.sbg.arena.core.event.PlayerAnimationFinishedEvent
import com.google.common.eventbus.Subscribe
import com.sbg.arena.core.event.PlayerAnimationStartedEvent

class Renderer(val configuration: Configuration,
               val level: Level,
               val levelSkin: Skin,
               val eventBus: EventBus) {
    private val logger = LogManager.getLogger(javaClass<Renderer>())!!

    private var camera = Camera(configuration)

    private var activeAnimations: MutableList<Animation> = ArrayList<Animation>()

    fun update() {
        activeAnimations filter { it.isFinished() } forEach { eventBus.post(PlayerAnimationFinishedEvent(it.request())) }

        activeAnimations = activeAnimations.filter { !it.isFinished() } as MutableList
        activeAnimations.forEach { it.update() }

        camera.update(level.playerCoordinates)
    }

    fun render(graphics: Graphics) {
        graphics!!.setBackground(Color.white)

        val viewArea = calculateViewArea()

        camera.renderGameplay(graphics) {
            levelSkin.render(level, viewArea)

            activeAnimations.forEach { it.render(graphics!!) }
        }
    }

    private fun calculateViewArea(): Rectangle {
        val playerCoordinates = level.playerCoordinates

        val leftTilesAvailable   = playerCoordinates.x
        val rightTilesAvailable  = level.width - playerCoordinates.x

        val topTilesAvailable    = playerCoordinates.y
        val bottomTilesAvailable = level.height - playerCoordinates.y

        if (leftTilesAvailable >= 15 && rightTilesAvailable >= 15 && topTilesAvailable >= 15 && bottomTilesAvailable >= 15) {
            return Rectangle(playerCoordinates.let { Point(it.x - 15, it.y - 15) },
                    playerCoordinates.let { Point(it.x + 15, it.y + 15) })
        }

        var numberOfLeftTiles  = if (leftTilesAvailable >= 15)  15 else leftTilesAvailable
        var numberOfRightTiles = if (rightTilesAvailable >= 15) 15 else rightTilesAvailable

        if (numberOfLeftTiles < 15)
            numberOfRightTiles += 15 - numberOfLeftTiles

        if (numberOfRightTiles < 15)
            numberOfLeftTiles += 15 - numberOfRightTiles

        var numberOfTopTiles    = if (topTilesAvailable >= 15)    15 else topTilesAvailable
        var numberOfBottomTiles = if (bottomTilesAvailable >= 15) 15 else bottomTilesAvailable

        if (numberOfTopTiles < 15)
            numberOfBottomTiles += 15 - numberOfTopTiles

        if (numberOfBottomTiles < 15)
            numberOfTopTiles += 15 - numberOfBottomTiles

        val start = Point(playerCoordinates.x - numberOfLeftTiles,
                playerCoordinates.y - numberOfTopTiles)
        val end = Point(playerCoordinates.x + numberOfRightTiles,
                playerCoordinates.y + numberOfBottomTiles)

        return Rectangle(start, end)
    }

    Subscribe
    fun playerAnimationStartedHandler(event: PlayerAnimationStartedEvent) {
        val request = event.request

        when (request) {
            is MoveRequest -> {
                val animation = MoveAnimation(request, levelSkin)
                animation.initialize()

                activeAnimations.add(animation)
            }
        }
    }
}