package com.sbg.arena.core.procedural_content_generation

import com.sbg.arena.core.Dimension

enum class FloorType {
    Floor
    Wall
}


trait Generator {
    fun generate(dimension: Dimension): Array<FloorType>
}