package com.sbg.arena.core.level

import org.newdawn.slick.Image
import com.sbg.arena.core.Level
import com.sbg.arena.util.withIndices
import com.sbg.arena.configuration.Configuration
import kotlin.properties.Delegates

class Skin(val configuration: Configuration) {
    private var floorTile: Image by Delegates.notNull()
    private var wallTile: Image by Delegates.notNull()
    private var playerTile: Image by Delegates.notNull()

    fun loadTiles() {
        val skinsDirectory = "assets/${configuration.skin}"

        floorTile  = Image("${skinsDirectory}/FloorTile.png")
        wallTile   = Image("${skinsDirectory}/WallTile.png")
        playerTile = Image("${skinsDirectory}/PlayerTile.png")
    }

    fun floorTile(): Image {
        return floorTile
    }

    fun wallTile(): Image {
        return wallTile
    }

    fun playerTile(): Image {
        return playerTile
    }

    fun render(level: Level) {
        val tileWidth = configuration.tileWidth
        val tileHeight = configuration.tileHeight

        for ((point, floor) in level.withIndices()) {
            val x = point.x.toFloat() * tileWidth
            val y = point.y.toFloat() * tileHeight

            when (floor) {
                FloorType.Floor  -> floorTile().draw(x, y)
                FloorType.Wall   -> wallTile().draw(x, y)
                FloorType.Player -> playerTile().draw(x, y)
            }
        }
    }
}