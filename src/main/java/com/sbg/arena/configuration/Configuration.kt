package com.sbg.arena.configuration

import kotlin.properties.Delegates

data class Configuration(val map: Map<String, Any?>) {
    val width: Int by Delegates.mapVal(map)
    val height: Int by Delegates.mapVal(map)
    val fullScreen: Boolean by Delegates.mapVal(map)
}