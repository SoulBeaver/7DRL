package com.sbg.arena.core.procedural_content_generation

import org.spek.Spek
import com.sbg.arena.configuration.loadConfiguration
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import com.sbg.arena.core.Dimension

class CaveGeneratorSpec: Spek() {{
    given("A cave generator with sensible configuration") {
        val configurationUrl = javaClass<CaveGeneratorSpec>().getClassLoader()!!.getResource("settings.yml")!!
        val configuration = loadConfiguration(configurationUrl.getPath()!!.substring(1))
        val caveGenerator = CaveGenerator(configuration)

        on("generating a 50x50 cave") {
            val cave = caveGenerator.generate(Dimension(50, 50))

            it("should be 2500 floors large") {
                assertEquals(50*50, cave.size)
            }

            it("should be partially filled with walls") {
                val numberOfWalls = cave.count { it == FloorType.Wall }

                assertTrue(numberOfWalls != 0)
            }
        }
    }
}}
