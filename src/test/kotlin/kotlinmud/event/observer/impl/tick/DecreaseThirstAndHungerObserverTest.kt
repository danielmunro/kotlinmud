package kotlinmud.event.observer.impl.tick

import assertk.assertThat
import assertk.assertions.isLessThan
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
}
