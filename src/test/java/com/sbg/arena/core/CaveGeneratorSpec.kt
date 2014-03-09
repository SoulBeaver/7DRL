package com.sbg.arena.core

import org.spek.Spek
import com.sbg.arena.configuration.loadConfiguration

class CaveGeneratorSpec: Spek() {{
    given("A cave generator with sensible configuration") {
        val configurationUrl = javaClass<CaveGeneratorSpec>().getClassLoader()!!.getResource("settings.yml")!!
        val configuration = loadConfiguration(configurationUrl.getPath()!!.substring(1))
        val caveGenerator = CaveGenerator(configuration)

        on("generating a 50x50 cave") {
            val cave = caveGenerator.generate(Dimension(50, 50))

            it("should be partially filled with walls") {

            }
        }
    }
}}
