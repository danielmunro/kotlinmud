package kotlinmud.event.observer.impl.client

import assertk.assertThat
import assertk.assertions.isNotNull
import kotlinmud.event.factory.createClientConnectedEvent
import kotlinmud.test.helper.createTestService
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
        assertThat(test.getAuthStep(client)).isNotNull()
    }
}
