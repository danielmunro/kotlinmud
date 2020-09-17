package kotlinmud.generator

import assertk.assertThat
import assertk.assertions.doesNotContain
import kotlinmud.biome.helper.createBiomes
import kotlinmud.generator.service.BiomeService
import kotlinmud.helper.layerToString
import kotlinmud.test.createTestService
import org.junit.Test

const val WIDTH = 1000
const val HEIGHT = 1000
const val BIOME_COUNT = 50

class BiomeServiceTest {
    @Test
    fun testCreateLayerSanityCheck() {
        // setup
        createTestService()

        // given
        val biomeService = BiomeService(
            WIDTH,
            HEIGHT,
            createBiomes()
        )

        // when
        val data = layerToString(biomeService.createLayer(BIOME_COUNT))

        // then
        assertThat(data).doesNotContain("0")
    }
}
