package kotlinmud.event.observer.tick

import assertk.assertThat
import assertk.assertions.hasSize
import kotlinmud.biome.type.SubstrateType
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.impl.tick.GenerateGrassObserver
import kotlinmud.event.type.EventType
import kotlinmud.test.createTestServiceWithResetDB
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class GenerateGrassObserverTest {
    @Test
    fun testCanGenerateGrass() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val room = test.createRoom {
            it.substrate = SubstrateType.DIRT
        }

        // expect
        assertThat(transaction { room.resources.toList() }).hasSize(0)

        // when
        runBlocking { GenerateGrassObserver().invokeAsync(Event(EventType.TICK, null)) }

        // then
        assertThat(transaction { room.resources.toList() }).hasSize(1)
    }
}
