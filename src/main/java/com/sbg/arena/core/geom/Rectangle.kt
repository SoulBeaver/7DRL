package com.sbg.arena.core.geom

class Rectangle(val topLeft: Point, val bottomRight: Point) {
    fun contains(candidate: Point): Boolean {
        return (candidate.x >= topLeft.x && candidate.y >= topLeft.y &&
                candidate.x <= bottomRight.x && candidate.y <= bottomRight.y)
    }
}