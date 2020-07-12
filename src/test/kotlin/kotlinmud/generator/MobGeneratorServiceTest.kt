package kotlinmud.generator

import assertk.assertThat
import assertk.assertions.isNotEmpty
import kotlinmud.biome.helper.createBiomes
import kotlinmud.test.createTestService
import org.junit.Test

class MobGeneratorServiceTest {
    @Test
    fun canGenerateMobResets() {
        // setup
        createTestService()
        val biomes = createBiomes()

        // given
        val generatorService = GeneratorService(10, 10, biomes)

        // when
        val world = generatorService.generate()

        // then
        assertThat(world.mobs).isNotEmpty()
        assertThat(world.mobResets).isNotEmpty()
    }
}
