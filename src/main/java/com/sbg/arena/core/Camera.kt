package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import com.sbg.arena.core.geom.Point
import kotlin.properties.Delegates
import org.newdawn.slick.Graphics
import org.apache.logging.log4j.LogManager

class Camera(val configuration: Configuration) {
    private val logger = LogManager.getLogger(javaClass<Camera>())!!

    private var cameraCenter = Point(0, 0)

    val viewportWidth: Int
    val viewportHeight: Int
    val worldWidth: Int
    val worldHeight: Int

    private val maximumOffset: Dimension
    private val minimumOffset: Dimension;

    {
        viewportWidth  = configuration.viewportWidth
        viewportHeight = configuration.viewportHeight
        worldWidth     = configuration.columns * configuration.tileWidth
        worldHeight    = configuration.rows * configuration.tileHeight

        maximumOffset = Dimension(worldWidth - viewportWidth / 2,
                                  worldHeight - viewportHeight / 2)
        minimumOffset = Dimension(0, 0)
    }

    fun update(centerOn: Point) {
        logger.debug("CenterOn coordinates:  (${centerOn.x}, ${centerOn.y})")

        var cameraX = (centerOn.x * configuration.tileWidth)  - viewportWidth / 2
        var cameraY = (centerOn.y * configuration.tileHeight) - viewportHeight / 2

        if (cameraX > maximumOffset.width)
            cameraX = maximumOffset.width
        else if (cameraX < minimumOffset.width)
            cameraX = minimumOffset.width

        if (cameraY > maximumOffset.height)
            cameraY = maximumOffset.height
        else if (cameraY < minimumOffset.height)
            cameraY = minimumOffset.height

        cameraCenter = Point(cameraX, cameraY)
        logger.debug("Camera Center:  (${cameraCenter.x}, ${cameraCenter.y})")
    }

    /**
     * Renders the scene at the current camera position.
     *
     * @param graphics The graphics device to render the screen
     * @param use Function block in which the currently rendered scene is to be filled
     */
    fun renderGameplay(graphics: Graphics, use: () -> Unit) {
        graphics.translate(-cameraCenter.x.toFloat(), -cameraCenter.y.toFloat())

        use()

        graphics.translate(cameraCenter.x.toFloat(), cameraCenter.y.toFloat())
    }
}