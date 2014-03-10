package com.sbg.arena.core.level

import org.newdawn.slick.Image
import kotlin.properties.Delegates

class BlackAndWhiteSkin: Skin {
    private var floorTile: Image by Delegates.notNull()
    private var wallTile: Image by Delegates.notNull()

    override fun loadTiles() {
        floorTile = Image("BlackAndWhite/FloorTile.png")
        wallTile  = Image("BlackAndWhite/WallTile.png")
    }

    override fun floorTile(): Image {
        return floorTile
    }

    override fun wallTile(): Image {
        return wallTile
    }
}