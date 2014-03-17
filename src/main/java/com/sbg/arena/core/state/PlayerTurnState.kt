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
import com.sbg.arena.core.KeyMap
import com.sbg.arena.core.Player
import org.apache.logging.log4j.LogManager
import kotlin.properties.Delegates
import com.sbg.arena.core.Level
import com.sbg.arena.core.Camera
import com.sbg.arena.configuration.Configuration
import com.sbg.arena.core.InputController
import com.sbg.arena.core.input.InputRequest
import com.sbg.arena.core.Direction
import com.sbg.arena.core.input.MoveRequest
import com.sbg.arena.core.Renderer
import com.sbg.arena.core.toAnimation
import com.sbg.arena.core.input.ToggleWallRequest

class PlayerTurnState(val configuration: Configuration,
                      val level: Level,
                      val player: Player,
                      val renderer: Renderer): BasicGameState() {
    private val logger = LogManager.getLogger(javaClass<PlayerTurnState>())!!

    private var game: StateBasedGame by Delegates.notNull()
    private var gameContainer: GameContainer by Delegates.notNull()

    private var inputController: InputController by Delegates.notNull()
    private var inputRequests: Map<String, () -> InputRequest> by Delegates.notNull()

    override fun init(gameContainer: GameContainer?, game: StateBasedGame?) {
        this.game = game!!
        this.gameContainer = gameContainer!!

        inputController = InputController(configuration)

        inputRequests = mapOf(configuration.moveUp    to { MoveRequest(level, player, Direction.North) },
                              configuration.moveDown  to { MoveRequest(level, player, Direction.South) },
                              configuration.moveLeft  to { MoveRequest(level, player, Direction.West)  },
                              configuration.moveRight to { MoveRequest(level, player, Direction.East)  },

                              configuration.toggleWallUp    to { ToggleWallRequest(level, player, Direction.North) },
                              configuration.toggleWallDown  to { ToggleWallRequest(level, player, Direction.South) },
                              configuration.toggleWallLeft  to { ToggleWallRequest(level, player, Direction.West) },
                              configuration.toggleWallRight to { ToggleWallRequest(level, player, Direction.East) })
                              /*
                              KeyMap[configuration.shoot]!! to ::castRayRequest)
                              */
    }

    override fun update(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        delta: Int) {
        if (!renderer.hasAnimationsPlaying()) {
            renderer.onAllAnimationsFinished { game!!.enterState(EnemyTurnState.ID) }

            enteredInputs().filter { it.isValid() }
                           .forEach { renderer.play(toAnimation(it), { it.execute() }) }
        }

        renderer.update()
    }

    private fun enteredInputs(): List<InputRequest> {
        val isKeyPressed = { (key: String) -> inputController.isKeyPressed(gameContainer, key) }

        return inputRequests.entrySet().filter { isKeyPressed(it.getKey()) }.map { it.getValue()() }
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
        val ID = 1
    }
}