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

val logger = LogManager.getLogger("Main")!!

fun main(args: Array<String>) {
    if (args.size != 1) {
        logger.error("Expected path to the configuration file.")
        System.exit(1)
    }

    val configuration = readConfiguration(args[0])

    Arena(configuration).run()
}

private fun readConfiguration(file: String): Configuration {
    val configurationPath = Paths.get(file)!!.toAbsolutePath()!!

    Preconditions.checkArgument(Files.exists(configurationPath),
                                "The configuration file $file does not exist!")

    val configurationMap = Files.newBufferedReader(configurationPath, StandardCharsets.UTF_8).use {
        Yaml().load(it) as Map<String, Any?>
    }

    return Configuration(configurationMap)
}