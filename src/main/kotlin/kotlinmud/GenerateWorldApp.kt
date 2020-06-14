package kotlinmud

import kotlinmud.app.createContainer
import kotlinmud.biome.helper.createBiomes
import kotlinmud.fs.service.PersistenceService
import kotlinmud.world.generation.GeneratorService
import kotlinmud.world.model.World
import org.kodein.di.erased.instance

fun main() {
    val app = createContainer(0)
    val world = GeneratorService(100, 100, createBiomes()).generate()
    val persistenceService by app.instance<PersistenceService>()
    persistenceService.writeWorld(World.fromGenerator(world))
}
