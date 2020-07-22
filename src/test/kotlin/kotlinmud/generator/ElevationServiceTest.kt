package kotlinmud.generator

import assertk.assertThat
import assertk.assertions.hasSize
import kotlinmud.biome.helper.createBiomes
import kotlinmud.test.createTestService
import org.junit.Test

class ElevationServiceTest {
    @Test
    fun testCreateElevationLayerWithCorrectSize() {
        createTestService()

        // setup
        val biomeService = BiomeService(
            width,
            length,
            createBiomes()
        )

        // given
        val elevationService = ElevationService(
            biomeService.createLayer(50),
            createBiomes()
        )

        // when
        val layer = elevationService.buildLayer()

        // then
        assertThat(layer).hasSize(length)
        assertThat(layer[0]).hasSize(width)
    }
}
