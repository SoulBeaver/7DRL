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

class Arena(val configuration: Configuration): BasicGame(configuration.gameTitle) {
    private val logger = LogManager.getLogger(javaClass<Arena>())!!
    private val levelGenerator: Generator
    private val cave: Array<FloorType>

    {
        levelGenerator = when (configuration.levelGenerator) {
            "cave" -> CaveGenerator(configuration)
            else   -> throw IllegalArgumentException("Generation strategy ${configuration.levelGenerator} not recognized")
        }

        cave = levelGenerator.generate(Dimension(configuration.columns, configuration.rows))
    }

    override fun init(gc: GameContainer?) {
        // Nothing to do yet
    }

    override fun update(gc: GameContainer?, i: Int) {

    }

    override fun render(gc: GameContainer?, g: Graphics?) {
        for ((index, floor) in cave) {

        }
    }
}