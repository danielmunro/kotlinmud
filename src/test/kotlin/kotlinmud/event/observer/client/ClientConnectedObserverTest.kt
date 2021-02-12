package kotlinmud.event.observer.client

import assertk.assertThat
import assertk.assertions.isNull
import kotlinmud.event.factory.createClientConnectedEvent
import kotlinmud.test.createTestService
import org.junit.Test

class ClientConnectedObserverTest {
    @Test
    fun testConnectedClientsAddToPlayerService() {
        // setup
        val test = createTestService()
        val client = test.getClient()

        // when
        test.callClientConnectedEvent(createClientConnectedEvent(client))

        // then
        assertThat(test.getAuthStep(client)).isNull()
    }
}
