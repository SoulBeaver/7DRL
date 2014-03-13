package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import org.apache.logging.log4j.LogManager
import com.sbg.arena.core.procedural_content_generation.CaveGenerator
import com.sbg.arena.core.procedural_content_generation.Generator
import com.sbg.arena.core.input.toggleWallUp
import com.sbg.arena.core.input.toggleWallDown
import com.sbg.arena.core.input.toggleWallLeft
import com.sbg.arena.core.input.toggleWallRight
import org.newdawn.slick.BasicGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import kotlin.properties.Delegates
import org.newdawn.slick.Color
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.geom.Rectangle
import org.newdawn.slick.state.StateBasedGame
import com.sbg.arena.core.state.MainMenuState
import com.sbg.arena.core.state.DefeatState
import com.sbg.arena.core.state.VictoryState
import com.sbg.arena.core.state.PlayerTurnState
import com.sbg.arena.core.state.EnemyTurnState

class Arena(val configuration: Configuration): StateBasedGame(configuration.gameTitle) {
    private var logger = LogManager.getLogger(javaClass<Arena>())!!

    private var levelGenerator: Generator by Delegates.notNull()
    private var level: Level by Delegates.notNull()
    private var levelSkin: Skin by Delegates.notNull()
    private var player: Player by Delegates.notNull()

    override fun initStatesList(gameContainer: GameContainer?) {
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

        addState(MainMenuState(configuration))

        val renderer = Renderer(configuration, level, levelSkin)
        addState(PlayerTurnState(configuration, level, player, renderer))
        addState(EnemyTurnState(configuration, level, player, renderer))

        addState(VictoryState(configuration))
        addState(DefeatState(configuration))
    }
}