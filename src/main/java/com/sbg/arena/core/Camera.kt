package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import com.sbg.arena.core.geom.Point
import kotlin.properties.Delegates
import org.newdawn.slick.Graphics
import org.apache.logging.log4j.LogManager

class Camera(val configuration: Configuration) {
    private val logger = LogManager.getLogger(javaClass<Camera>())!!

    private var cameraCenter = Point(0, 0)

    private val viewportWidth: Int
    private val viewportHeight: Int
    private val worldWidth: Int
    private val worldHeight: Int

    private val maximumOffset: Dimension
    private val minimumOffset: Dimension;

    {
        viewportWidth  = configuration.viewportWidth
        viewportHeight = configuration.viewportHeight
        worldWidth     = configuration.columns * configuration.tileWidth
        worldHeight    = configuration.rows    * configuration.tileHeight

        maximumOffset = Dimension(worldWidth  - viewportWidth,
                                  worldHeight - viewportHeight)
        minimumOffset = Dimension(0, 0)
    }

    /**
     * Update and center the camera on the current point, or against the edges of the screen if not
     * enough space remains.
     *
     * @param centerOn The focus of the camera
     */
    fun update(centerOn: Point) {
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