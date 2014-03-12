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

class PlayerTurnState(val configuration: Configuration,
                val level: Level,
                val levelSkin: Skin,
                val player: Player): BasicGameState() {
    private val logger = LogManager.getLogger(javaClass<PlayerTurnState>())!!

    private var inputController: InputController by Delegates.notNull()
    private var inputRequests: Map<Int, () -> InputRequest> by Delegates.notNull()

    private var camera: Camera by Delegates.notNull()

    override fun init(gameContainer: GameContainer?, game: StateBasedGame?) {
        inputController = InputController(configuration)

        inputRequests = mapOf(KeyMap[configuration.moveUp]!!    to { MoveRequest(level, player, Direction.North) },
                              KeyMap[configuration.moveDown]!!  to { MoveRequest(level, player, Direction.North) },
                              KeyMap[configuration.moveLeft]!!  to { MoveRequest(level, player, Direction.North) },
                              KeyMap[configuration.moveRight]!! to { MoveRequest(level, player, Direction.North) })
                              /*
                              KeyMap[configuration.toggleWallUp]!!    to ::toggleWallUpRequest,
                              KeyMap[configuration.toggleWallDown]!!  to ::toggleWallDownRequest,
                              KeyMap[configuration.toggleWallLeft]!!  to ::toggleWallLeftRequest,
                              KeyMap[configuration.toggleWallRight]!! to ::toggleWallRightRequest,

                              KeyMap[configuration.shoot]!! to ::castRayRequest)
                              */

        camera = Camera(configuration)

        camera.update(level.playerCoordinates)
    }

    override fun update(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        delta: Int) {
        /*
        if (currentState == SubState.PlayerTurnState) {
            val userInputs = inputController.update(gameContainer!!)

            for (userInput in userInputs) {
                val request = requests[userInput.keyCode]!!()

                if (request.isValid())
                    animationPlayer.enqueue(animations[request])
            }
        }

        if (currentState == SubState.AnimatingState) {
            if (!animationPlayer.isFinished()) {
                animationPlayer.update()
            } else {
                request.execute()
            }
        }
        */

        camera.update(level.playerCoordinates)
    }

    override fun render(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        graphics: Graphics?) {
        graphics!!.setBackground(Color.white)

        val viewArea = calculateViewArea()

        camera.renderGameplay(graphics) {
            levelSkin.render(level, viewArea)
        }
    }

    private fun calculateViewArea(): Rectangle {
        val playerCoordinates = level.playerCoordinates

        /*
        val tileOffset = Dimension(-1 * camera.x % configuration.tileWidth,
                                   -1 * camera.y % configuration.tileHeight)
        val start = Point(camera.x / configuration.tileWidth,
                          camera.y / configuration.tileHeight)
        val end = Point((camera.width - tileOffset.width) / configuration.tileWidth + 1,
                        (camera.height - tileOffset.height) / configuration.tileHeight + 1)

        return Rectangle(start, end)
        */

        val leftTilesAvailable   = playerCoordinates.x
        val rightTilesAvailable  = level.width - playerCoordinates.x

        val topTilesAvailable    = playerCoordinates.y
        val bottomTilesAvailable = level.height - playerCoordinates.y

        if (leftTilesAvailable >= 15 && rightTilesAvailable >= 15 && topTilesAvailable >= 15 && bottomTilesAvailable >= 15) {
            return Rectangle(playerCoordinates.let { Point(it.x - 15, it.y - 15) },
                    playerCoordinates.let { Point(it.x + 15, it.y + 15) })
        }

        var numberOfLeftTiles  = if (leftTilesAvailable >= 15)  15 else leftTilesAvailable
        var numberOfRightTiles = if (rightTilesAvailable >= 15) 15 else rightTilesAvailable

        if (numberOfLeftTiles < 15)
            numberOfRightTiles += 15 - numberOfLeftTiles

        if (numberOfRightTiles < 15)
            numberOfLeftTiles += 15 - numberOfRightTiles

        var numberOfTopTiles    = if (topTilesAvailable >= 15)    15 else topTilesAvailable
        var numberOfBottomTiles = if (bottomTilesAvailable >= 15) 15 else bottomTilesAvailable

        if (numberOfTopTiles < 15)
            numberOfBottomTiles += 15 - numberOfTopTiles

        if (numberOfBottomTiles < 15)
            numberOfTopTiles += 15 - numberOfBottomTiles

        val start = Point(playerCoordinates.x - numberOfLeftTiles,
                playerCoordinates.y - numberOfTopTiles)
        val end = Point(playerCoordinates.x + numberOfRightTiles,
                playerCoordinates.y + numberOfBottomTiles)

        return Rectangle(start, end)
    }

    override fun getID(): Int {
        return ID
    }

    class object {
        val ID = 1
    }
}