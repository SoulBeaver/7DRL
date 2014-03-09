package com.sbg.arena.core

import com.sbg.arena.configuration.Configuration
import com.google.common.base.Preconditions
import java.util.ArrayList
import java.util.Random
import com.sbg.arena.util.bindFirst

enum class FloorType {
    Floor
    Wall
}

/**
 * CaveGenerator's purpose is to create an asymmetrical, open-ended cave with few openings.
 * Algorithm for CaveGenerator inspired (stolen) from:
 *
 * http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels
 */
class CaveGenerator(val configuration: Configuration) {
    private val random = Random()

    /**
     * Generates an asymmetrical, open-ended cave.
     *
     * @param dimension The target width and height of the cave
     * @return the floor layout of the cave.
     */
    fun generate(dimension: Dimension): Array<FloorType> {
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

        // First pass, create walls when probability exceeds threshold
        val cave = Array<FloorType>(width * height, {
            if (random.nextInt(100) < wallCreationProbability)
                FloorType.Wall
            else
                FloorType.Floor
        })

        for (pass in 1..numberOfPasses) {
            cave.withIndices().map {
                floorWithIndex -> {
                    val (index, floor) = floorWithIndex

                    val neighbors = neighbors(dimension, cave, index)
                    val neighborsWhichAreWalls = neighbors.count { it == FloorType.Wall }

                    if (floor == FloorType.Wall) {
                        if (neighborsWhichAreWalls < neighborsRequiredToRemainAWall)
                            FloorType.Floor
                    }
                    else {
                        if (neighborsWhichAreWalls >= neighborsRequiredToCreateAWall)
                            FloorType.Wall
                    }
                }
            }
        }

        return cave
    }

    /**
     * Returns the list of all neighbors at floorIndex in cave.
     *
     * @param dimension The Dimensions of the cave
     * @param cave The context for the floorIndex
     * @param floorIndex The current floor in the cave
     * @return all adjacent neighbors to floorIndex; size may vary depending on edges, corners of floorIndex
     */
    private fun neighbors(dimension: Dimension, cave: Array<FloorType>, floorIndex: Int): List<FloorType> {
        /**
         * To understand what's happening here, consider a 3x3 matrix of Floors
         * where W = Wall, F = Floor.
         *
         * W F W
         * F F F
         * W F W
         *
         * Our floorIndex is the center of this matrix, or (1, 1) in xy coordinates.
         * However, cave is a single list, so the cells are laid out as such:
         *
         * W F W F F F W F W
         *
         * The floorIndex is now 4 (starting from 0).
         *
         * It's trivial to get the left and right neighbors by adding or subtracting 1
         * To get the bottom and top neighbors, consider again the first 3x3 matrix.
         *
         * W F W
         * F F F
         * W F W
         *
         * A cave still has width and height, so subtracting width from our current floorIndex will result int
         *
         *  n = floorIndex - width
         *  n = 4 - 3
         *  n = 1
         *
         *  If 1 is the new index, then we can see that it is the F in the first row, the top neighbor to our
         *  current FloorIndex.
         *
         *  It should now be easy to see that adding width will yield the bottom F.
         *
         *  Adding or subtracting 1 to the bottom and top neighbors will yield their respective right and left
         *  neighbors, or the diagonals of our floor index. It is thus possible to return all 8 neighbors of floorIndex.
         */
        return listOf(
                cave[floorIndex - 1],
                cave[floorIndex + 1],
                cave[floorIndex - dimension.width],
                cave[floorIndex + dimension.width],
                cave[floorIndex - 1 - dimension.width],
                cave[floorIndex + 1 - dimension.width],
                cave[floorIndex - 1 + dimension.width],
                cave[floorIndex + 1 + dimension.width]
        )
    }
}

