package kotlinmud.event.observer.client

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.impl.client.LogPlayerOutObserver
import kotlinmud.event.type.EventType
import kotlinmud.player.repository.findMobCardByName
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class LogPlayerOutObserverTest {
    @Test
    fun testThatMobCardLoggedInIsSetToFalse() {
        // setup
        val test = createTestService()
        val client1 = test.createClient()
        client1.mob = test.createPlayerMob()
        val client2 = test.createClient()
        client2.mob = test.createPlayerMob()

        // given
        transaction {
            client1.mob!!.mobCard!!.loggedIn = true
            client2.mob!!.mobCard!!.loggedIn = true
        }

        // when
        transaction {
            LogPlayerOutObserver().processEvent(Event(EventType.CLIENT_DISCONNECTED, client1))
        }

        // then
        findMobCardByName(client1.mob!!.name)!!.let { assertThat(it.loggedIn).isFalse() }
        findMobCardByName(client2.mob!!.name)!!.let { assertThat(it.loggedIn).isTrue() }
    }
}
