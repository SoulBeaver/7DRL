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

    private val camera = Camera(configuration)

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
        
        camera.update(playerCoordinates)
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

        camera.update(playerCoordinates)
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

    override fun render(gameContainer: GameContainer?, graphics: Graphics?) {
        graphics!!.setBackground(Color.white)

        camera.renderGameplay(graphics) {
            levelSkin.render(level)
        }
    }
}