package kotlinmud.generator

import assertk.assertThat
import assertk.assertions.hasSameSizeAs
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import kotlinmud.biome.helper.createBiomes
import kotlinmud.biome.type.BiomeType
import kotlinmud.generator.constant.DEPTH
import org.junit.Test

const val length = 10
const val width = 12

class GeneratorServiceTest {
    @Test
    fun testGeneratorCreatesMatrix() {
        // given
        val generator = GeneratorService(
            width,
            length,
            createBiomes()
        )

        // when
        val world = generator.generate()

        // then
        assertThat(world.matrix3D.size).isEqualTo(DEPTH)
        assertThat(world.matrix3D[0].size).isEqualTo(length)
        assertThat(world.matrix3D[0][0].size).isEqualTo(width)
    }

    @Test
    fun testMatrixReceivesBiomes() {
        // given
        val generator = GeneratorService(
            width,
            length,
            createBiomes()
        )

        // when
        val world = generator.generate()

        // then
        assertThat(world.rooms.all {
            it.biome != BiomeType.NONE
        }).isTrue()
    }

    @Test
    fun testIndexesAreUnique() {
        // setup
        val generator = GeneratorService(
            width,
            length,
            createBiomes()
        )
        val indexes = mutableListOf<Int>()
        val world = generator.generate()

        // given
        world.matrix3D.forEach { z ->
            z.forEach { y ->
                y.forEach { z ->
                    indexes.add(z)
                }
            }
        }

        // expect
        assertThat(indexes).hasSameSizeAs(indexes.distinct())
    }
}
