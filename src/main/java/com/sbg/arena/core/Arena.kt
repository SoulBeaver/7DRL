package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.Display
import org.apache.logging.log4j.LogManager
import com.sbg.arena.core.procedural_content_generation.CaveGenerator
import com.sbg.arena.core.procedural_content_generation.Generator
import org.newdawn.slick.BasicGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import kotlin.properties.Delegates
import org.newdawn.slick.Image
import org.newdawn.slick.Color
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.level.FloorType
import com.sbg.arena.core.geom.Point

class Arena(val configuration: Configuration): BasicGame(configuration.gameTitle) {
    private val logger = LogManager.getLogger(javaClass<Arena>())!!

    private var levelGenerator: Generator by Delegates.notNull()
    private var level: Level by Delegates.notNull()
    private var levelSkin: Skin by Delegates.notNull()
    private var player: Player by Delegates.notNull()
    private var playerCoordinates: Point by Delegates.notNull()

    private var cameraCenter: Point by Delegates.notNull()
    private var maxOffsetX: Int by Delegates.notNull()
    private var maxOffsetY: Int by Delegates.notNull()
    private var minOffsetX = 0
    private var minOffsetY = 0

    override fun init(gc: GameContainer?) {
        levelGenerator = when (configuration.levelGenerator) {
            "cave" -> CaveGenerator(configuration)
            else   -> throw IllegalArgumentException("Generation strategy ${configuration.levelGenerator} not recognized")
        }

        level = levelGenerator.generate(Dimension(configuration.columns, configuration.rows))
        logger.debug(level.toString())

        levelSkin = Skin(configuration)
        levelSkin.loadTiles()

        player = Player(configuration)
        playerCoordinates = placePlayer()

        cameraCenter = playerCoordinates

        val worldWidth  = level.width * configuration.tileWidth
        val worldHeight = level.height * configuration.tileHeight

        maxOffsetX = worldWidth - configuration.width
        maxOffsetY = worldHeight - configuration.height
    }

    /**
     * Place the player within the level
     */
    private fun placePlayer(): Point {
        // Naive algorithm:  place the player in the first open cell from the top.
        for ((point, floor) in level.withIndices()) {
            if (floor == FloorType.Floor) {
                level[point] = FloorType.Player
                return point
            }
        }

        throw IllegalStateException("Every tile in the level was a Wall, please check the map.")
    }

    override fun update(gc: GameContainer?, delta: Int) {
        val requestedDirection = player.move(gc!!)

        if (requestedDirection != null) {
            when (requestedDirection) {
                Direction.SOUTH -> tryMove(Point(playerCoordinates.x, playerCoordinates.y + 1))
                Direction.NORTH -> tryMove(Point(playerCoordinates.x, playerCoordinates.y - 1))
                Direction.EAST  -> tryMove(Point(playerCoordinates.x + 1, playerCoordinates.y))
                Direction.WEST  -> tryMove(Point(playerCoordinates.x - 1, playerCoordinates.y))
            }
        }

        logger.debug("Player coordinates:  (${playerCoordinates.x}, ${playerCoordinates.y})")
        var cameraX = (playerCoordinates.x * configuration.tileWidth) - configuration.width / 2
        var cameraY = (playerCoordinates.y * configuration.tileHeight) - configuration.height / 2

        if (cameraX > maxOffsetX)
            cameraX = maxOffsetX
        else if (cameraX < minOffsetX)
            cameraX = minOffsetX

        if (cameraY > maxOffsetY)
            cameraY = maxOffsetY
        else if (cameraY < minOffsetY)
            cameraY = minOffsetY

        cameraCenter = Point(cameraX, cameraY)
        logger.debug("Camera Center:  (${cameraCenter.x}, ${cameraCenter.y})")
    }

    private fun tryMove(destination: Point) {
        if (destination.x < 0 || destination.y < 0 ||
            destination.x >= configuration.columns || destination.y >= configuration.rows)
            return

        if (level[destination] == FloorType.Floor) {
            level[destination] = FloorType.Player
            level[playerCoordinates] = FloorType.Floor

            playerCoordinates = destination
        }
    }

    override fun render(gc: GameContainer?, g: Graphics?) {
        g!!.setBackground(Color.white)

        g.translate(-(cameraCenter.x.toFloat()), -(cameraCenter.y.toFloat()))

        levelSkin.render(level)
    }
}