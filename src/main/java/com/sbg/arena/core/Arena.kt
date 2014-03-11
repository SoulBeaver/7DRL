package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
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
import org.newdawn.slick.Color
import com.sbg.arena.core.level.Skin
import com.sbg.arena.core.geom.Point
import com.sbg.arena.core.geom.Rectangle
import org.newdawn.slick.state.StateBasedGame
import com.sbg.arena.core.state.MainMenuState
import com.sbg.arena.core.state.DefeatState
import com.sbg.arena.core.state.VictoryState
import com.sbg.arena.core.state.GameState

class Arena(val configuration: Configuration): StateBasedGame(configuration.gameTitle) {
    override fun initStatesList(gameContainer: GameContainer?) {
        addState(GameState(configuration))
        addState(MainMenuState(configuration))
        addState(VictoryState(configuration))
        addState(DefeatState(configuration))
    }
}