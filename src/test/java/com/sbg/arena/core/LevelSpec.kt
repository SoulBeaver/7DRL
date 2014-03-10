package com.sbg.arena.core.level

import org.spek.Spek
import com.sbg.arena.core.Dimension
import com.sbg.arena.core.Level
import com.sbg.arena.core.iterator
import com.sbg.arena.core.procedural_content_generation.FloorType
import kotlin.test.assertEquals
import kotlin.test.failsWith

class LevelSpec: Spek() {{
    given("A 3x3 Level") {
        val level = Level(Dimension(3, 3), array(FloorType.Wall, FloorType.Floor, FloorType.Wall,
                                                  FloorType.Floor, FloorType.Floor, FloorType.Floor,
                                                  FloorType.Wall, FloorType.Floor, FloorType.Wall))

        on("inspecting the area") {
            val area = level.area

            it("should be nine") {
                assertEquals(3 * 3, area)
            }
        }

        on("getting arbitrary coordinates") {
            val topLeft     = level[0, 0]
            val top         = level[1, 0]
            val topRight    = level[2, 0]

            val centerLeft  = level[0, 1]
            val center      = level[1, 1]
            val centerRight = level[2, 1]

            val bottomLeft  = level[0, 2]
            val bottom      = level[1, 2]
            val bottomRight = level[2, 2]

            it("should return the correct FloorType") {
                assertEquals(FloorType.Wall, topLeft)
                assertEquals(FloorType.Floor, top)
                assertEquals(FloorType.Wall, topRight)

                assertEquals(FloorType.Floor, centerLeft)
                assertEquals(FloorType.Floor, center)
                assertEquals(FloorType.Floor, centerRight)

                assertEquals(FloorType.Wall, bottomLeft)
                assertEquals(FloorType.Floor, bottom)
                assertEquals(FloorType.Wall, bottomRight)
            }
        }

        on("iterating through all the floors") {
            val levelIterator = level.iterator()

            it("should iterate exactly nine times") {
                var iterations = 0
                for (floor in level)
                    iterations += 1

                assertEquals(9, iterations)
            }

            it("should iterate in order") {
                assertEquals(FloorType.Wall, levelIterator.next())
                assertEquals(FloorType.Floor, levelIterator.next())
                assertEquals(FloorType.Wall, levelIterator.next())

                assertEquals(FloorType.Floor, levelIterator.next())
                assertEquals(FloorType.Floor, levelIterator.next())
                assertEquals(FloorType.Floor, levelIterator.next())

                assertEquals(FloorType.Wall, levelIterator.next())
                assertEquals(FloorType.Floor, levelIterator.next())
                assertEquals(FloorType.Wall, levelIterator.next())
            }

            it ("should throw IndexOutOfBoundsException on iterating past end") {
                failsWith(javaClass<IndexOutOfBoundsException>()) {
                    levelIterator.next()
                }
            }
        }

        on("iterating with index") {
            it("should ") {
                
            }
        }
    }
}}