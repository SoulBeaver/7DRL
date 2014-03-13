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

class PlayerTurnState(val configuration: Configuration,
                      val level: Level,
                      val player: Player,
                      val renderer: Renderer): BasicGameState() {
    private val logger = LogManager.getLogger(javaClass<PlayerTurnState>())!!

    private var inputController: InputController by Delegates.notNull()
    private var inputRequests: Map<String, () -> InputRequest> by Delegates.notNull()

    private var executingRequests = listOf<InputRequest>() as MutableList

    override fun init(gameContainer: GameContainer?, game: StateBasedGame?) {
        inputController = InputController(configuration)

        inputRequests = mapOf(configuration.moveUp    to { MoveRequest(level, player, Direction.North) },
                              configuration.moveDown  to { MoveRequest(level, player, Direction.South) },
                              configuration.moveLeft  to { MoveRequest(level, player, Direction.West)  },
                              configuration.moveRight to { MoveRequest(level, player, Direction.East)  })
                              /*
                              KeyMap[configuration.toggleWallUp]!!    to ::toggleWallUpRequest,
                              KeyMap[configuration.toggleWallDown]!!  to ::toggleWallDownRequest,
                              KeyMap[configuration.toggleWallLeft]!!  to ::toggleWallLeftRequest,
                              KeyMap[configuration.toggleWallRight]!! to ::toggleWallRightRequest,

                              KeyMap[configuration.shoot]!! to ::castRayRequest)
                              */

        renderer.addOnAnimationFinishedHandler { finishedAnimatingHandler(game!!, it) }
    }

    override fun update(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        delta: Int) {
        if (executingRequests.isEmpty()) {
            val isKeyPressed = { (key: String) -> inputController.isKeyPressed(gameContainer!!, key) }

            for (inputRequest in inputRequests) {
                if (isKeyPressed(inputRequest.getKey()) && inputRequest.getValue()().isValid()) {
                    val currentRequest = inputRequest.getValue()()
                    executingRequests.add(currentRequest)
                    renderer.enqueue(currentRequest)
                }
            }
        }

        renderer.update()
    }

    private fun finishedAnimatingHandler(game: StateBasedGame, request: InputRequest) {
        request.execute()

        executingRequests.remove(request)
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