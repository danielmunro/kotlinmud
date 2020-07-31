package kotlinmud

import kotlinmud.biome.helper.createBiomes
import kotlinmud.db.applySchema
import kotlinmud.db.createConnection
import kotlinmud.generator.GeneratorService

fun main() {
    createConnection()
    applySchema()
    GeneratorService(100, 100, createBiomes()).generate()
}
