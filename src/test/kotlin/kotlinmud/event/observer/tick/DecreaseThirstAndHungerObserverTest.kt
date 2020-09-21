package kotlinmud.event.observer.tick

import assertk.assertThat
import assertk.assertions.isLessThan
import kotlinmud.event.factory.createTickEvent
import kotlinmud.player.repository.findMobCardByName
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
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
        transaction { test.getDecreaseThirstAndHungerObserver().processEvent(createTickEvent()) }

        // then
        findMobCardByName(client.mob!!.name)!!.let {
            assertThat(it.thirst).isLessThan(client.mob!!.race.maxThirst)
            assertThat(it.hunger).isLessThan(client.mob!!.race.maxAppetite)
        }
    }
}
