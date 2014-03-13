package com.sbg.arena.core.state

import com.sbg.arena.configuration.Configuration
import org.apache.logging.log4j.LogManager
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.Graphics
import com.sbg.arena.core.Player
import com.sbg.arena.core.Level
import com.sbg.arena.core.Camera
import kotlin.properties.Delegates
import org.newdawn.slick.Color
import com.sbg.arena.core.Renderer

class EnemyTurnState(val configuration: Configuration,
                     val level: Level,
                     val player: Player,
                     val renderer: Renderer): BasicGameState() {
    private val logger = LogManager.getLogger(javaClass<EnemyTurnState>())!!

    override fun init(gameContainer: GameContainer?, game: StateBasedGame?) {

    }

    override fun update(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        delta: Int) {
        renderer.update()
        game!!.enterState(PlayerTurnState.ID)
    }

    override fun render(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        graphics: Graphics?) {
        renderer.render(graphics!!)
    }

    override fun getID(): Int {
        return ID
    }

    class object {
        val ID = 6
    }
}