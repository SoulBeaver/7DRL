package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.Display
import org.apache.logging.log4j.LogManager

class Arena(val configuration: Configuration) {
    private val logger = LogManager.getLogger(javaClass<Arena>())!!

    {
        Display.setDisplayMode(DisplayMode(configuration.width, configuration.height))
    }

    fun run() {
        Display.create()

        while (!Display.isCloseRequested()) {
            Display.update()
        }

        Display.destroy()
    }
}