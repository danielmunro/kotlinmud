package kotlinmud.event.observer.client

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlinmud.test.helper.createTestService
import org.junit.Test

class LogPlayerOutObserverTest {
    @Test
    fun testThatMobCardLoggedInIsSetToFalse() {
        // setup
        val test = createTestService()
        val client1 = test.createClient()
        client1.mob = test.createPlayerMob().also { it.loggedIn = true }
        val client2 = test.createClient()
        client2.mob = test.createPlayerMob().also { it.loggedIn = true }

        // when
        test.callLogPlayerOutObserver(client1)

        // then
        client1.mob!!.let { assertThat(it.loggedIn).isFalse() }
        client2.mob!!.let { assertThat(it.loggedIn).isTrue() }
    }

    @Test
    fun testWorksWithClientWithoutMobs() {
        // setup
        val test = createTestService()
        val client = test.createClient()

        // when
        test.callLogPlayerOutObserver(client)

        // then
        assertThat(client.mob).isNull()
    }

    @Test
    fun testLogoutRemovesMobFromMobService() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMob()
        val client = test.createClient().also {
            it.mob = mob
        }

        // when
        test.callLogPlayerOutObserver(client)

        // then
        assertThat(test.findPlayerMob(mob.name)).isNull()
    }
}
