package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.test.createTestServiceWithResetDB
import org.junit.Test

const val emailAddress = "dan@danmunro.com"

class EmailAuthStepTest {
    @Test
    fun testPlayerIsCreatedWhenNotFound() {
        // setup
        val test = createTestServiceWithResetDB()

        // when
        val response = test.runPreAuth(emailAddress)

        // then
        assertThat(response.message).isEqualTo("ok")
        assertThat(findPlayerByEmail(emailAddress)).isNotNull()
    }

    @Test
    fun testPlayerCanBeFound() {
        // setup
        val test = createTestServiceWithResetDB()

        test.createPlayer(emailAddress)

        // when
        val response = test.runPreAuth(emailAddress)

        // then
        assertThat(response.message).isEqualTo("ok")
        assertThat(findPlayerByEmail(emailAddress)).isNotNull()
    }
}
