package com.sbg.arena.core.state

import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.BasicGameState
import com.sbg.arena.configuration.Configuration
import org.apache.logging.log4j.LogManager
import org.newdawn.slick.Color
import kotlin.properties.Delegates
import org.newdawn.slick.TrueTypeFont
import java.awt.Font
import org.newdawn.slick.Image
import com.sbg.arena.core.InputController
import org.newdawn.slick.Input
import org.newdawn.slick.state.transition.FadeOutTransition
import org.newdawn.slick.state.transition.FadeInTransition

class MainMenuState(val configuration: Configuration): BasicGameState() {
    val logger = LogManager.getLogger(javaClass<MainMenuState>())!!

    var mainMenuBackground: Image by Delegates.notNull()

    val fontName = configuration.mainMenuFont

    var titleFont: TrueTypeFont by Delegates.notNull()
    var optionFont: TrueTypeFont by Delegates.notNull()

    val title       = configuration.mainMenuTitle
    val playOption  = configuration.mainMenuPlayOption
    val leaveOption = configuration.mainMenuLeaveOption

    var inputController: InputController by Delegates.notNull()

    override fun init(gameContainer: GameContainer?, game: StateBasedGame?) {
        gameContainer!!.setShowFPS(false)
        gameContainer.setTargetFrameRate(30)
        gameContainer.setMaximumLogicUpdateInterval(10)
        gameContainer.setVSync(true)

        titleFont = TrueTypeFont(Font(fontName, Font.PLAIN, configuration.mainMenuTitleSize),
                                 antiAlias = true)
        optionFont = TrueTypeFont(Font(fontName, Font.PLAIN, configuration.mainMenuOptionSize),
                                  antiAlias = true)

        mainMenuBackground = Image("assets/${configuration.mainMenuBackground}")

        inputController = InputController(configuration)
    }

    override fun update(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        delta: Int) {
        val inputs = inputController.update(gameContainer!!)

        if (inputs.find { it.keyCode == Input.KEY_1 } != null) {
            game!!.enterState(GameState.ID,
                              FadeOutTransition(Color.black),
                              FadeInTransition(Color.black))
        } else if (inputs.find { it.keyCode == Input.KEY_2 } != null) {
            gameContainer.exit()
        }
    }

    override fun render(gameContainer: GameContainer?,
                        game: StateBasedGame?,
                        graphics: Graphics?) {
        graphics!!.setColor(Color.white)

        graphics.drawImage(mainMenuBackground, 0F, 0F)

        titleFont.drawString(configuration.viewportWidth.toFloat() / 2 - 50,
                             configuration.viewportHeight.toFloat() / 5,
                             title)

        optionFont.drawString(configuration.viewportWidth.toFloat() / 2 - 80,
                              configuration.viewportHeight.toFloat() / 2,
                              playOption)
        optionFont.drawString(configuration.viewportWidth.toFloat() / 2 - 80,
                              configuration.viewportHeight.toFloat() / 2 + 60,
                              leaveOption)
    }

    override fun getID(): Int {
        return ID
    }

    class object {
        val ID = 0
    }
}