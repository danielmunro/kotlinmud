package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class PasswordAuthStepTest {
    @Test
    fun testSanity() {
        // setup
        val test = createTestServiceWithResetDB()
        test.createPlayer(emailAddress)

        // given
        test.runPreAuth(emailAddress)

        // when
        val player = findPlayerByEmail(emailAddress)!!
        val response = test.runPreAuth(transaction { player.lastOTP!! })

        // then
        assertThat(response.message).isEqualTo("Success.")
    }
}
