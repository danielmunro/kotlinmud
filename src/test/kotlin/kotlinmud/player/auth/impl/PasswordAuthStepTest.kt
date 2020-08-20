package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

const val incorrectPassword = "yoyoma"

class PasswordAuthStepTest {
    @Test
    fun testCanUseOTPToLogIn() {
        // setup
        val test = createTestServiceWithResetDB()
        test.createPlayer(emailAddress)

        // given
        test.runPreAuth(emailAddress)

        // when
        val player = findPlayerByEmail(emailAddress)!!
        val response = test.runPreAuth(transaction { player.lastOTP!! })

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCannotUseBadOTP() {
        // setup
        val test = createTestServiceWithResetDB()
        test.createPlayer(emailAddress)

        // given
        test.runPreAuth(emailAddress)

        // when
        val response = test.runPreAuth(incorrectPassword)

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }

    @Test
    fun testPlayerRequiresOTP() {
        // setup
        val test = createTestServiceWithResetDB()
        test.createPlayer(emailAddress)

        // given
        test.runPreAuth(emailAddress)
        val player = findPlayerByEmail(emailAddress)!!
        transaction { player.lastOTP = "" }

        // when
        val response = test.runPreAuth(incorrectPassword)

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }

    @Test
    fun testRequiresTheRightOTP() {
        // setup
        val test = createTestServiceWithResetDB()
        test.createPlayer(emailAddress)

        // given
        test.runPreAuth(emailAddress)

        // when
        val response = test.runPreAuth(incorrectPassword)

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }

    @Test
    fun testCannotSwitchUsersWithADifferentOTP() {
        // setup
        val test = createTestServiceWithResetDB()
        val emailAddress2 = "stealth@danmunro.com"
        val player1 = test.createPlayer(emailAddress)
        val player2 = test.createPlayer(emailAddress2)
        transaction {
            player1.lastOTP = "1"
            player2.lastOTP = "2"
        }

        // given
        test.runPreAuth(emailAddress)

        // when
        val response = test.runPreAuth("2")

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }
}
