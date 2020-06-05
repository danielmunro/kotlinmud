package kotlinmud.world.generation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import kotlinmud.world.BiomeType
import org.junit.Test

const val length = 96
const val width = 128

class GeneratorTest {
    @Test
    fun testGeneratorCreatesMatrix() {
        // given
        val generator = Generator(width, length)

        // when
        val world = generator.generate()

        // then
        assertThat(world.blocks.size).isEqualTo(DEPTH)
        assertThat(world.blocks[0].size).isEqualTo(length)
        assertThat(world.blocks[0][0].size).isEqualTo(width)
    }

    @Test
    fun testMatrixReceivesBiomes() {
        // given
        val generator = Generator(width, length)

        // when
        val world = generator.generate()

        // then
        assertThat(world.rooms.all {
            it.biome != BiomeType.NONE
        }).isTrue()
    }
}
