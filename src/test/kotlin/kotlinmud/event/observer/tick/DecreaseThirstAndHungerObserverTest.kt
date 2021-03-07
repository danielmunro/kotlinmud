package kotlinmud.event.observer.tick

import assertk.assertThat
import assertk.assertions.isLessThan
import io.mockk.confirmVerified
import io.mockk.verify
import kotlinmud.event.factory.createTickEvent
import kotlinmud.test.helper.createTestService
import org.junit.Test

class DecreaseThirstAndHungerObserverTest {
    @Test
    fun testThatMobDecreasesThirstAndHunger() {
        // setup
        val test = createTestService()

        // given
        val client = test.getClient()
        client.mob = test.createPlayerMob()

        // when
        test.callDecreaseThirstAndHungerEvent(createTickEvent())

        // then
        client.mob!!.let {
            assertThat(it.thirst).isLessThan(client.mob!!.race.maxThirst)
            assertThat(it.hunger).isLessThan(client.mob!!.race.maxAppetite)
        }
    }

    @Test
    fun testCanHandleNonLoggedInClients() {
        // setup
        val test = createTestService()

        // given
        test.getClient()

        // when
        test.callDecreaseThirstAndHungerEvent(createTickEvent())
    }

    @Test
    fun testThatMobGetsMessagingForHungerAndThirst() {
        // setup
        val test = createTestService()
        val client = test.getClient()

        // given
        client.mob = test.createPlayerMob()

        // when
        test.callDecreaseThirstAndHungerEvent(createTickEvent())

        // then
        verify {
            client.mob = any()
            client.mob
            client.writePrompt("You are hungry.")
            client.write(any())
            client.writePrompt("You are thirsty.")
            client.write(any())
        }
        confirmVerified(client)
    }
}
