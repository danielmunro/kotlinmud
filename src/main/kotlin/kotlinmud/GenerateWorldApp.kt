package kotlinmud

import kotlinmud.app.createContainer
import kotlinmud.biome.helper.createBiomes
import kotlinmud.generator.GeneratorService

fun main() {
    createContainer(0)
    GeneratorService(100, 100, createBiomes()).generate()
}
