package kotlinmud.event.observer.tick

import assertk.assertThat
import assertk.assertions.hasSize
import kotlinmud.biome.type.SubstrateType
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class GenerateGrassObserverTest {
    @Test
    fun testCanGenerateGrass() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val room = test.createRoomBuilder()
                .substrate(SubstrateType.DIRT)
                .build()

        // expect
        assertThat(transaction { room.resources.toList() }).hasSize(0)

        // when
        test.callGenerateGrassObserver()

        // then
        assertThat(transaction { room.resources.toList() }).hasSize(1)
    }
}
