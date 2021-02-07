package kotlinmud.generator

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import kotlinmud.biome.type.BiomeType
import kotlinmud.generator.constant.DEPTH
import kotlinmud.test.createTestService
import org.junit.Test

const val length = 5
const val width = 5

class WorldGenerationTest {
    @Test
    fun testGeneratorCreatesMatrix() {
        // setup
        val test = createTestService()

        // given
        val generation = test.createWorldGeneration(width, length)

        // when
        val world = generation.world!!

        // then
        assertThat(world.matrix3D.size).isEqualTo(DEPTH)
        assertThat(world.matrix3D[0].size).isEqualTo(length)
        assertThat(world.matrix3D[0][0].size).isEqualTo(width)
    }

    @Test
    fun testMatrixReceivesBiomes() {
        // setup
        val test = createTestService()

        // given
        val generation = test.createWorldGeneration(width, length)

        // when
        val world = generation.world!!

        // then
        assertThat(
            world.rooms.all {
                it.biome != BiomeType.NONE
            }
        ).isTrue()
    }
}
