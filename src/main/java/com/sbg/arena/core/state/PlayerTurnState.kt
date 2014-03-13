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
import com.google.common.eventbus.Subscribe
import com.sbg.arena.core.event.PlayerAnimationFinishedEvent
import com.google.common.eventbus.EventBus
import com.sbg.arena.core.event.AllAnimationsFinishedEvent
import com.sbg.arena.core.event.PlayerAnimationStartedEvent

class PlayerTurnState(val configuration: Configuration,
                      val level: Level,
                      val player: Player,
                      val eventBus: EventBus): BasicGameState() {
    private val logger = LogManager.getLogger(javaClass<PlayerTurnState>())!!

    private var game: StateBasedGame by Delegates.notNull()
    private var gameContainer: GameContainer by Delegates.notNull()

    private var inputController: InputController by Delegates.notNull()
    private var inputRequests: Map<String, () -> InputRequest> by Delegates.notNull()

    private var executingRequests = listOf<InputRequest>() as MutableList

    override fun init(gameContainer: GameContainer?, game: StateBasedGame?) {
        this.game = game!!
        this.gameContainer = gameContainer!!

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
    }

    override fun update(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        delta: Int) {
        if (executingRequests.isEmpty()) {
            val isKeyPressed = { (key: String) -> inputController.isKeyPressed(gameContainer!!, key) }

            for (inputRequest in inputRequests) {
                val request = inputRequest.getValue()()

                if (isKeyPressed(inputRequest.getKey()) && request.isValid())
                    eventBus.post(PlayerAnimationStartedEvent(request))
            }
        }
    }

    Subscribe
    private fun finishedAnimatingHandler(event: PlayerAnimationFinishedEvent) {
        val request = event.request
        request.execute()
        executingRequests.remove(request)
    }

    Subscribe
    private fun allAnimationsinishedHandler(event: AllAnimationsFinishedEvent) {
        eventBus.unregister(this)
        game.enterState(EnemyTurnState.ID)
    }

    override fun render(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        graphics: Graphics?) {

    }

    override fun getID(): Int {
        return ID
    }

    class object {
        val ID = 1
    }
}