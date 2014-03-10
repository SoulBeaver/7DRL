package com.sbg.arena.core.procedural_content_generation

import com.sbg.arena.core.Dimension
import com.sbg.arena.core.Level

trait Generator {
    fun generate(dimension: Dimension): Level
}