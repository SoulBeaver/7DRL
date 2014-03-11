package com.sbg.arena.configuration

import kotlin.properties.Delegates
import java.nio.file.Paths
import com.google.common.base.Preconditions
import java.nio.file.Files
import org.yaml.snakeyaml.Yaml
import java.nio.charset.StandardCharsets

data class Configuration(val map: Map<String, Any?>) {
    /*
     * General options
     */
    val gameTitle: String by Delegates.mapVal(map)

    /*
     * Video options
     */
    val width: Int by Delegates.mapVal(map)
    val height: Int by Delegates.mapVal(map)
    val fullScreen: Boolean by Delegates.mapVal(map)
    val viewportWidth: Int by Delegates.mapVal(map)
    val viewportHeight: Int by Delegates.mapVal(map)

    /*
     * Generation options
     */
    val levelGenerator: String by Delegates.mapVal(map)
    val rows: Int by Delegates.mapVal(map)
    val columns: Int by Delegates.mapVal(map)
    val tileWidth: Int by Delegates.mapVal(map)
    val tileHeight: Int by Delegates.mapVal(map)
    val skin: String by Delegates.mapVal(map)

    /*
     * Cave generation options
     *
     * @see http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels
     */
    val wallCreationProbability: Int by Delegates.mapVal(map)
    val neighborsRequiredToRemainAWall: Int by Delegates.mapVal(map)
    val neighborsRequiredToCreateAWall: Int by Delegates.mapVal(map)
    val numberOfPasses: Int by Delegates.mapVal(map)

    /*
     * Player options
     */
    val hitPoints: Int by Delegates.mapVal(map)
    val attack: Int by Delegates.mapVal(map)

    /*
     * Key bindings
     */
    val moveUp: String by Delegates.mapVal(map)
    val moveLeft: String by Delegates.mapVal(map)
    val moveDown: String by Delegates.mapVal(map)
    val moveRight: String by Delegates.mapVal(map)

    val toggleWallUp: String by Delegates.mapVal(map)
    val toggleWallDown: String by Delegates.mapVal(map)
    val toggleWallLeft: String by Delegates.mapVal(map)
    val toggleWallRight: String by Delegates.mapVal(map)

    val shoot: String by Delegates.mapVal(map)

    /*
     * Main Menu Options
     */
    val mainMenuBackground: String by Delegates.mapVal(map)

    val mainMenuFont: String by Delegates.mapVal(map)
    val mainMenuTitle: String by Delegates.mapVal(map)
    val mainMenuTitleSize: Int by Delegates.mapVal(map)

    val mainMenuPlayOption: String by Delegates.mapVal(map)
    val mainMenuLeaveOption: String by Delegates.mapVal(map)
    val mainMenuOptionSize: Int by Delegates.mapVal(map)

    assert {
        // TODO:  Assertions!
    }
}

fun loadConfiguration(filename: String): Configuration {
    val configurationPath = Paths.get(filename)!!.toAbsolutePath()!!

    Preconditions.checkArgument(Files.exists(configurationPath),
                                "The configuration file $filename does not exist!")

    val configurationMap = Files.newBufferedReader(configurationPath, StandardCharsets.UTF_8).use {
        Yaml().load(it) as Map<String, Any?>
    }

    return Configuration(configurationMap)
}