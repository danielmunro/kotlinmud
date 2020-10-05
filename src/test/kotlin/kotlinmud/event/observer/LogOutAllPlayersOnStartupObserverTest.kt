package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isFalse
import kotlinmud.player.repository.findMobCardByName
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class LogOutAllPlayersOnStartupObserverTest {
    @Test
    fun testThatAllMobsAreLoggedOutOnStartup() {
        // setup
        val test = createTestService()
        val mob1 = test.createPlayerMob()
        val mob2 = test.createPlayerMob()

        // given
        transaction {
            mob1.mobCard!!.loggedIn = true
            mob2.mobCard!!.loggedIn = true
        }

        // when
        test.callLogoutPlayersOnStartupEvent()

        // then
        transaction {
            assertThat(findMobCardByName(mob1.name)!!.loggedIn).isFalse()
            assertThat(findMobCardByName(mob2.name)!!.loggedIn).isFalse()
        }
    }
}
