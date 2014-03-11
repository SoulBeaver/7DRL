package com.sbg.arena.core.state

import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.BasicGameState
import com.sbg.arena.configuration.Configuration

class MainMenuState(val configuration: Configuration): BasicGameState() {
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
        return 0
    }
}