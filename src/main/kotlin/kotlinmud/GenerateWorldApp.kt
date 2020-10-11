package kotlinmud

import kotlinmud.biome.helper.createBiomes
import kotlinmud.db.applySchema
import kotlinmud.db.createConnection
import kotlinmud.generator.config.GeneratorConfig
import kotlinmud.generator.service.BiomeService
import kotlinmud.generator.service.WorldGeneration
import kotlinmud.generator.statemachine.createStateMachine
import kotlinmud.generator.statemachine.runStateMachine

const val width = 100
const val length = 100

fun main() {
    createConnection()
    applySchema()
    val stateMachine = createStateMachine(
        GeneratorConfig(width, length),
        BiomeService(width, length, createBiomes()),
        WorldGeneration()
    )
    runStateMachine(stateMachine)
}
