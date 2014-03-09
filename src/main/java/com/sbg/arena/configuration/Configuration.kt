package com.sbg.arena.configuration

import kotlin.properties.Delegates
import java.nio.file.Paths
import com.google.common.base.Preconditions
import java.nio.file.Files
import org.yaml.snakeyaml.Yaml
import java.nio.charset.StandardCharsets

data class Configuration(val map: Map<String, Any?>) {
    /*
     * Video options
     */
    val width: Int by Delegates.mapVal(map)
    val height: Int by Delegates.mapVal(map)
    val fullScreen: Boolean by Delegates.mapVal(map)

    /*
     * Cave generation options
     *
     * @see http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels
     */
    val wallCreationProbability: Int by Delegates.mapVal(map)
    val neighborsRequiredToRemainAWall: Int by Delegates.mapVal(map)
    val neighborsRequiredToCreateAWall: Int by Delegates.mapVal(map)
    val numberOfPasses: Int by Delegates.mapVal(map)
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