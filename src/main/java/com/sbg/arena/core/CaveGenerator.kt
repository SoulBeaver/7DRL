package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import com.google.common.base.Preconditions
import java.util.ArrayList
import java.util.Random
import com.sbg.arena.util.bindFirst

private enum class FloorType {
    Floor
    Wall
}

private val random = Random()

fun generateCave(dimension: Dimension, configuration: Configuration): Array<FloorType> {
    val (width, height) = dimension

    Preconditions.checkArgument(width > 0 && height > 0,
                                "Cannot generate a map with negative width or height")

    val numberOfPasses = configuration.numberOfPasses
    val wallCreationProbability = configuration.wallCreationProbability
    val neighborsRequiredToRemainAWall = configuration.neighborsRequiredToRemainAWall
    val neighborsRequiredToCreateAWall = configuration.neighborsRequiredToCreateAWall

    Preconditions.checkArgument(numberOfPasses > 0,
                                "Cannot generate a map without at least one pass")
    Preconditions.checkArgument(wallCreationProbability < 100,
                                "If wall creation is set to 100%, the map will be one huge wall.")
    Preconditions.checkArgument(neighborsRequiredToRemainAWall > 0 && neighborsRequiredToRemainAWall <= 9,
                                "NeighborsRequiredToRemainAWall describes the number of adjacent neighbors required" +
                                " for a wall to remain a wall. This cannot be less than 0 or greater than 9.")
    Preconditions.checkArgument(neighborsRequiredToCreateAWall > 0 && neighborsRequiredToCreateAWall <= 9,
                                "NeighborsRequiredToCreateAWall describes the number of adjacent neighbors required" +
                                " for a space to become a wall. This cannot be less than 0 or greater than 9.")

    val cave = Array<FloorType>(width * height, ::generateWall.bindFirst(wallCreationProbability))

    return cave
}

private fun generateWall(wallCreationProbability: Int, index: Int): FloorType {
    return if (random.nextInt(100) < wallCreationProbability)
               FloorType.Wall
           else
               FloorType.Floor
}