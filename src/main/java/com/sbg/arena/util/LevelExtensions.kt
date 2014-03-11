package com.sbg.arena.util

import com.sbg.arena.core.Level
import com.sbg.arena.core.level.FloorType
import com.sbg.arena.core.geom.Point

fun Level.toAsciiString(): String {
    val asciiLevel = Array<String>(area, {
        if (get(it) == FloorType.Floor) "." else "#"
    })

    val asciiRepresentationBuilder = StringBuilder()
    for ((index, floor) in asciiLevel.withIndices()) {
        if (index % dimension.width == 0)
            asciiRepresentationBuilder.append(System.lineSeparator())

        asciiRepresentationBuilder.append(floor)
    }

    return asciiRepresentationBuilder.toString()
}

fun Level.iterable(): Iterable<FloorType> {
    val outer = this

    return object: Iterable<FloorType> {
        override fun iterator(): Iterator<FloorType> {
            return outer.iterator()
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