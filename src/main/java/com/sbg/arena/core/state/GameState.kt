package com.sbg.arena.core.state

import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Graphics
import com.sbg.arena.core.geom.Rectangle
import com.sbg.arena.core.geom.Point
import org.newdawn.slick.Color
import com.sbg.arena.core.procedural_content_generation.CaveGenerator
import com.sbg.arena.core.Dimension
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.Player
import com.sbg.arena.core.InputController
import com.sbg.arena.core.KeyMap
import com.sbg.arena.core.input.moveUp
import com.sbg.arena.core.input.moveRight
import com.sbg.arena.core.input.moveLeft
import com.sbg.arena.core.input.moveDown
import com.sbg.arena.core.input.toggleWallUp
import com.sbg.arena.core.input.toggleWallDown
import com.sbg.arena.core.input.toggleWallLeft
import com.sbg.arena.core.input.toggleWallRight
import org.apache.logging.log4j.LogManager
import com.sbg.arena.core.procedural_content_generation.Generator
import kotlin.properties.Delegates
import com.sbg.arena.core.Level
import com.sbg.arena.core.Camera
import com.sbg.arena.core.InputType
import com.sbg.arena.configuration.Configuration

class GameState(val configuration: Configuration): BasicGameState() {
    private val logger = LogManager.getLogger(javaClass<GameState>())!!

    private var levelGenerator: Generator by Delegates.notNull()
    private var level: Level by Delegates.notNull()
    private var levelSkin: Skin by Delegates.notNull()
    private var player: Player by Delegates.notNull()

    private val camera = Camera(configuration)

    private var inputController: InputController by Delegates.notNull()
    private var inputCommands: Map<Int, (Level, InputType) -> Unit> by Delegates.notNull()

    override fun init(gameContainer: GameContainer?, game: StateBasedGame?) {
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

    override fun update(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        delta: Int) {
        val inputs = inputController.update(gameContainer!!)

        inputs.forEach {
            inputCommands[it.keyCode](level, it.inputType)
        }

        camera.update(level.playerCoordinates)
    }

    override fun render(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        graphics: Graphics?) {
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

    override fun getID(): Int {
        return 1
    }

    class object {
        val ID = 1
    }
}