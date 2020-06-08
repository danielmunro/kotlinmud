package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNotNull
import io.mockk.mockk
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.PlayerLoggedInEvent
import kotlinmud.io.model.Client
import kotlinmud.test.createTestService
import org.junit.Test

class LogPlayerInObserverTest {
    @Test
    fun testMobIsSetOnLoginEvent() {
        // setup
        val test = createTestService()
        val client = Client(mockk())

        // given
        val mob = test.createPlayerMobBuilder().build()
        test.addBuiltPlayerMob(mob)
        val mobCard = test.findMobCardByName(mob.name)!!

        // when
        test.publish(Event(EventType.CLIENT_LOGGED_IN, PlayerLoggedInEvent(client, mobCard)))

        // then
        assertThat(client.mob).isEqualTo(mob)
    }

    @Test
    fun testMobIsAddedToWorld() {
        // setup
        val test = createTestService()
        val client = Client(mockk())

        // given
        val mob = test.createPlayerMobBuilder().build()
        test.addBuiltPlayerMob(mob)
        val mobCard = test.findMobCardByName(mob.name)!!

        // expect
        assertThat {
            test.getRoomForMob(mob)
        }.isFailure()

        // when
        test.publish(Event(EventType.CLIENT_LOGGED_IN, PlayerLoggedInEvent(client, mobCard)))

        // then
        assertThat(test.getRoomForMob(mob)).isNotNull()
    }
}
