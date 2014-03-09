package com.sbg.arena.configuration

import kotlin.properties.Delegates

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