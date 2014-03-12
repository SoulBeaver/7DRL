package com.sbg.arena.core.state

import com.sbg.arena.configuration.Configuration
import org.apache.logging.log4j.LogManager
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.Graphics

class AnimatingGameState(val configuration: Configuration): BasicGameState() {
    val logger = LogManager.getLogger(javaClass<AnimatingGameState>())!!

    override fun init(gameContainer: GameContainer?, game: StateBasedGame?) {

    }

    override fun update(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        delta: Int) {

    }

    override fun render(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        graphics: Graphics?) {

    }

    override fun getID(): Int {
        return ID
    }

    class object {
        val ID = 7
    }
}
