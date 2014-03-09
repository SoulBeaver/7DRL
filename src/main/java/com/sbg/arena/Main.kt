package com.sbg

import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.apache.logging.log4j.LogManager
import java.nio.file.Paths
import org.yaml.snakeyaml.Yaml
import java.nio.file.Files
import java.nio.charset.StandardCharsets
import com.sbg.arena.configuration.Configuration
import org.yaml.snakeyaml.constructor.Constructor
import com.google.common.base.Preconditions
import com.sbg.arena.core.Arena
import com.sbg.arena.configuration.loadConfiguration
import org.newdawn.slick.AppGameContainer

val logger = LogManager.getLogger("Main")!!

fun main(args: Array<String>) {
    if (args.size != 1) {
        logger.error("Expected path to the configuration file.")
        System.exit(1)
    }

    val configuration = loadConfiguration(args[0])

    val appGameContainer = AppGameContainer(Arena(configuration))
    appGameContainer.setDisplayMode(configuration.width,
                                    configuration.height,
                                    configuration.fullScreen)
    appGameContainer.start()
}