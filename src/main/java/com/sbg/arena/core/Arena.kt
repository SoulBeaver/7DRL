package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.Display
import org.apache.logging.log4j.LogManager
import com.sbg.arena.core.procedural_content_generation.CaveGenerator
import com.sbg.arena.core.procedural_content_generation.Generator
import com.sbg.arena.core.procedural_content_generation.FloorType
import org.newdawn.slick.BasicGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import kotlin.properties.Delegates
import org.newdawn.slick.Image
import org.newdawn.slick.Color
import com.sbg.arena.core.level.Skin

class Arena(val configuration: Configuration): BasicGame(configuration.gameTitle) {
    private val logger = LogManager.getLogger(javaClass<Arena>())!!

    private var levelGenerator: Generator by Delegates.notNull()
    private var level: Level by Delegates.notNull()
    private var levelSkin: Skin by Delegates.notNull()

    override fun init(gc: GameContainer?) {
        levelGenerator = when (configuration.levelGenerator) {
            "cave" -> CaveGenerator(configuration)
            else   -> throw IllegalArgumentException("Generation strategy ${configuration.levelGenerator} not recognized")
        }

        level = levelGenerator.generate(Dimension(configuration.columns, configuration.rows))
        logger.debug(level.toString())

        levelSkin = Skin(configuration)
        levelSkin.loadTiles()
    }

    override fun update(gc: GameContainer?, i: Int) {
        // Nothing to do here yet
    }

    override fun render(gc: GameContainer?, g: Graphics?) {
        g!!.setBackground(Color.white)

        levelSkin.render(level)
    }
}