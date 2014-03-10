package com.sbg.arena.core.level

import org.newdawn.slick.Image

trait Skin {
    fun loadTiles()

    fun floorTile(): Image
    fun wallTile(): Image
}