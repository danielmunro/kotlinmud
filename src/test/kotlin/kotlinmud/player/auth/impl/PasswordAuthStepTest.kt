package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

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
        val response = test.runPreAuth("yoyoma")

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
        val response = test.runPreAuth("")

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }
}
