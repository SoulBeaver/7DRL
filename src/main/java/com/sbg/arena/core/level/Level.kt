package com.sbg.arena.core

import com.sbg.arena.core.procedural_content_generation.FloorType
import com.google.common.base.Preconditions
import org.apache.logging.log4j.LogManager
import kotlin.properties.Delegates
import com.sbg.arena.core.geom.Point

class Level(val dimension: Dimension,
            private val level: Array<FloorType>) {
    {
        val area = dimension.width * dimension.height
        Preconditions.checkArgument(area == level.size,
                                    "Level does not conform to the given dimensions; expected ${area}, but was ${level.size}")
    }

    private val logger = LogManager.getLogger(javaClass<Level>())

    private val asciiRepresentation: String by Delegates.lazy {
        val asciiLevel = Array<String>(level.size, {
            if (level[it] == FloorType.Floor) "." else "#"
        })

        val asciiRepresentationBuilder = StringBuilder()
        for ((index, floor) in asciiLevel.withIndices()) {
            if (index % dimension.width == 0)
                asciiRepresentationBuilder.append(System.lineSeparator())

            asciiRepresentationBuilder.append(floor)
        }
        asciiRepresentationBuilder.toString()
    }

    val width  = dimension.width
    val height = dimension.height
    val area   = width * height

    fun get(point: Point): FloorType {
        return get(point.x, point.y)
    }

    fun get(x: Int, y: Int): FloorType {
        return level[x + y * width]
    }

    fun toString():String {
        return asciiRepresentation
    }
}

fun Level.iterable(): Iterable<FloorType> {
    return object: Iterable<FloorType> {
        override fun iterator(): Iterator<FloorType> {
            return iterator()
        }
    }
}

fun Level.iterator(): Iterator<FloorType> {
    return object: Iterator<FloorType> {
        var x = 0
        var y = 0

        override fun hasNext(): Boolean {
            return (y != height)
        }

        override fun next(): FloorType {
            val next = get(x, y)

            x += 1
            if (x % width == 0) {
                y += 1
                x = 0
            }

            return next
        }
    }
}

fun Level.withIndices(): Iterator<Pair<Point, FloorType>> {
    return object: Iterator<Pair<Point, FloorType>> {
        var x = 0
        var y = 0

        override fun hasNext(): Boolean {
            return (y != height)
        }

        override fun next(): Pair<Point, FloorType> {
            val next = Point(x, y) to get(x, y)

            x += 1
            if (x % width == 0) {
                y += 1
                x = 0
            }

            return next
        }
    }
}