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
                              KeyMap[configuration.moveRight]!! to ::moveRight)
    }

    override fun update(gc: GameContainer?, delta: Int) {
        val inputs = inputController.update(gc!!)

        inputs.forEach {
            inputCommands[it](level)
        }

        camera.update(level.playerCoordinates)
    }

    override fun render(gameContainer: GameContainer?, graphics: Graphics?) {
        graphics!!.setBackground(Color.white)

        camera.renderGameplay(graphics) {
            levelSkin.render(level)
        }
    }
}