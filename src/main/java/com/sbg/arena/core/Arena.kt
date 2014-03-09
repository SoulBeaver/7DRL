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

class Arena(val configuration: Configuration): BasicGame(configuration.gameTitle) {
    private val logger = LogManager.getLogger(javaClass<Arena>())!!

    private var levelGenerator: Generator by Delegates.notNull()
    private var cave: Array<FloorType> by Delegates.notNull()

    private var floorTile: Image by Delegates.notNull()
    private var wallTile: Image by Delegates.notNull()

    override fun init(gc: GameContainer?) {
        levelGenerator = when (configuration.levelGenerator) {
            "cave" -> CaveGenerator(configuration)
            else   -> throw IllegalArgumentException("Generation strategy ${configuration.levelGenerator} not recognized")
        }

        cave = levelGenerator.generate(Dimension(configuration.columns, configuration.rows))
        logCave(cave)

        floorTile = Image("assets/FloorTile.png")
        wallTile  = Image("assets/WallTile.png")
    }

    override fun update(gc: GameContainer?, i: Int) {
        // Nothing to do here yet
    }

    override fun render(gc: GameContainer?, g: Graphics?) {
        var row = 0

        for ((index, floor) in cave.withIndices()) {
            if (index % 49 == 0)
                row += 20

            if (floor == FloorType.Wall)
                wallTile.draw(index * 20F, row * 20F)
            else
                floorTile.draw(index * 20F, row * 20F)
        }
    }

    private fun logCave(cave: Array<FloorType>) {
        val graphicalCave = cave.map { if (it == FloorType.Floor) "." else "#" }

        val stringBuilder = StringBuilder()
        for ((index, floor) in graphicalCave.withIndices()) {
            if (index % 50 == 0)
                stringBuilder.append(System.lineSeparator())

            stringBuilder.append(floor)
        }

        logger.debug(stringBuilder.toString())
    }
}