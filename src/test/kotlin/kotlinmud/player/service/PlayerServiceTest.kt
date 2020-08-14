package kotlinmud.player.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.repository.findLoggedInMobCards
import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class PlayerServiceTest {
    @Test
    fun testThatAllPlayersAreLoggedOut() {
        // setup
        val test = createTestService()

        val mob1 = test.createPlayerMob()
        val mob2 = test.createPlayerMob()
        val mob3 = test.createPlayerMob()

        // given
        transaction {
            mob1.mobCard?.loggedIn = true
            mob2.mobCard?.loggedIn = true
            mob3.mobCard?.loggedIn = true
        }

        // when
        test.logOutPlayers()

        // then
        assertThat(findLoggedInMobCards()).hasSize(0)
    }

    @Test
    fun testCreateNewPlayerErrorsOnBadEmailAddress() {
        // setup
        val test = createTestService()

        // expect
        assertThat { test.createPlayer("foo") }.isFailure()
    }

    @Test
    fun testCreateNewPlayerWorksWithValidEmail() {
        // setup
        val test = createTestServiceWithResetDB()

        // expect
        assertThat(test.createPlayer("foo@bar.com")).isInstanceOf(PlayerDAO::class)
    }
}
