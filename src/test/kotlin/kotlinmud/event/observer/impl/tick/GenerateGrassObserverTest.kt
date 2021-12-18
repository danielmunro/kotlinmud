package kotlinmud.event.observer.impl.tick

import assertk.assertThat
import assertk.assertions.hasSize
import kotlinmud.biome.type.SubstrateType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class GenerateGrassObserverTest {
    @Test
    fun testCanGenerateGrass() {
        // setup
        val test = createTestService()

        // given
        val room = test.createRoomBuilder()
            .also { it.substrate = SubstrateType.DIRT }
            .build()

        // expect
        assertThat(room.resources).hasSize(0)

        // when
        test.callGenerateGrassObserver()

        // then
        assertThat(room.resources).hasSize(1)
    }
}
