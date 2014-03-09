package com.sbg.arena.configuration

import org.spek.Spek
import kotlin.test.assertEquals

class ConfigurationSpec: Spek() {{
    given("A configuration file") {
        val configurationUrl = javaClass<ConfigurationSpec>().getClassLoader()!!.getResource("settings.yml")!!

        on("loading it") {
            val configuration = loadConfiguration(configurationUrl.getPath()!!.substring(1))

            it("should correctly load all configuration values") {
                assertEquals(1024, configuration.width)
                assertEquals(768, configuration.height)
                assertEquals(false, configuration.fullScreen)

                assertEquals(10, configuration.numberOfPasses)
                assertEquals(3, configuration.neighborsRequiredToRemainAWall)
                assertEquals(5, configuration.neighborsRequiredToCreateAWall)
                assertEquals(40, configuration.wallCreationProbability)

                assertEquals("cave", configuration.levelGenerator)
            }
        }
    }
}}