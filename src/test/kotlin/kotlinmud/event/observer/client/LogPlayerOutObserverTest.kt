package kotlinmud.event.observer.client

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlinmud.event.factory.createClientDisconnectedEvent
import kotlinmud.event.observer.impl.client.LogPlayerOutObserver
import kotlinmud.player.repository.findMobCardByName
import kotlinmud.test.helper.createTestService
import kotlinx.coroutines.runBlocking
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
            runBlocking {
                LogPlayerOutObserver().invokeAsync(
                    createClientDisconnectedEvent(client1)
                )
            }
        }

        // then
        findMobCardByName(client1.mob!!.name)!!.let { assertThat(it.loggedIn).isFalse() }
        findMobCardByName(client2.mob!!.name)!!.let { assertThat(it.loggedIn).isTrue() }
    }

    @Test
    fun testWorksWithClientWithoutMobs() {
        // setup
        val test = createTestService()
        val client = test.createClient()

        // when
        transaction {
            runBlocking {
                LogPlayerOutObserver().invokeAsync(
                    createClientDisconnectedEvent(client)
                )
            }
        }

        // then
        assertThat(client.mob).isNull()
    }
}
