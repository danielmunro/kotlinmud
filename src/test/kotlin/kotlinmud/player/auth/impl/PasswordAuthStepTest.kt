package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

const val incorrectPassword = "yoyoma"

class PasswordAuthStepTest {
    @Test
    fun testCanUseOTPToLogIn() {
        // setup
        val test = setup()
        val player = findPlayerByEmail(emailAddress)!!

        // when
        val response = runBlocking { test.runPreAuth(transaction { player.lastOTP!! }) }

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCannotUseBadOTP() {
        // setup
        val test = setup()

        // when
        val response = runBlocking { test.runPreAuth(incorrectPassword) }

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }

    @Test
    fun testPlayerRequiresOTP() {
        // setup
        val test = setup()

        // given
        val player = findPlayerByEmail(emailAddress)!!
        transaction { player.lastOTP = "" }

        // when
        val response = runBlocking { test.runPreAuth(incorrectPassword) }

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }

    @Test
    fun testRequiresTheRightOTP() {
        // setup
        val test = setup()

        // when
        val response = runBlocking { test.runPreAuth(incorrectPassword) }

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }

    @Test
    fun testCannotSwitchUsersWithADifferentOTP() {
        // setup
        val test = setup()
        val emailAddress2 = "stealth@danmunro.com"
        val player2 = test.createPlayer(emailAddress2)

        // given
        transaction {
            player2.lastOTP = "2"
        }

        // when
        val response = runBlocking { test.runPreAuth("2") }

        // then
        assertThat(response.message).isEqualTo("sorry, there was an error.")
    }

    private fun setup(): TestService {
        val test = createTestService()
        test.createPlayer(emailAddress)
        setPreAuth(test)
        return test
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> PasswordAuthStep(authStepService, player) }
    }
}
