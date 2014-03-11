package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.Display
import org.apache.logging.log4j.LogManager
import com.sbg.arena.core.procedural_content_generation.CaveGenerator
import com.sbg.arena.core.procedural_content_generation.Generator
import com.sbg.arena.core.input.moveUp
import com.sbg.arena.core.input.moveDown
import com.sbg.arena.core.input.moveLeft
import com.sbg.arena.core.input.moveRight
import com.sbg.arena.core.input.toggleWallUp
import com.sbg.arena.core.input.toggleWallDown
import com.sbg.arena.core.input.toggleWallLeft
import com.sbg.arena.core.input.toggleWallRight
import org.newdawn.slick.BasicGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import kotlin.properties.Delegates
import org.newdawn.slick.Image
import org.newdawn.slick.Color
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.level.FloorType
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.geom.Rectangle

class Arena(val configuration: Configuration): BasicGame(configuration.gameTitle) {
    private val logger = LogManager.getLogger(javaClass<Arena>())!!

    private var levelGenerator: Generator by Delegates.notNull()
    private var level: Level by Delegates.notNull()
    private var levelSkin: Skin by Delegates.notNull()
    private var player: Player by Delegates.notNull()

    private val camera = Camera(configuration)

    private var inputController: InputController by Delegates.notNull()
    private var inputCommands: Map<Int, (Level) -> Unit> by Delegates.notNull()

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
        level.placePlayer()
        
        camera.update(level.playerCoordinates)

        inputController = InputController(configuration)
        inputCommands = mapOf(KeyMap[configuration.moveUp]!!    to ::moveUp,
                              KeyMap[configuration.moveDown]!!  to ::moveDown,
                              KeyMap[configuration.moveLeft]!!  to ::moveLeft,
                              KeyMap[configuration.moveRight]!! to ::moveRight,

                              KeyMap[configuration.toggleWallUp]!!    to ::toggleWallUp,
                              KeyMap[configuration.toggleWallDown]!!  to ::toggleWallDown,
                              KeyMap[configuration.toggleWallLeft]!!  to ::toggleWallLeft,
                              KeyMap[configuration.toggleWallRight]!! to ::toggleWallRight)
    }

    override fun update(gc: GameContainer?, delta: Int) {
        val inputs = inputController.update(gc!!)

        inputs.forEach {
            inputCommands[it](level)
        }

        camera.update(level.playerCoordinates)
    }

    val viewport = Dimension(configuration.viewportWidth,
                             configuration.viewportHeight)
    val tileDimensions = Dimension(configuration.tileWidth,
                                   configuration.tileHeight)

    val drawableTilesInX = viewport.width / tileDimensions.width
    val drawableTilesInY = viewport.height / tileDimensions.height

    override fun render(gameContainer: GameContainer?, graphics: Graphics?) {
        graphics!!.setBackground(Color.white)

        val viewArea = calculateViewArea()

        camera.renderGameplay(graphics) {
            levelSkin.render(level, viewArea)
        }
    }

    private fun calculateViewArea(): Rectangle {
        val playerCoordinates = level.playerCoordinates

        val leftTilesAvailable   = playerCoordinates.x
        val rightTilesAvailable  = level.width - playerCoordinates.x

        val topTilesAvailable    = playerCoordinates.y
        val bottomTilesAvailable = level.height - playerCoordinates.y

        if (leftTilesAvailable >= 15 && rightTilesAvailable >= 15 &&
            topTilesAvailable >= 15 && bottomTilesAvailable >= 15) {
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
}