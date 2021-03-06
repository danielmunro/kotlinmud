package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.test.helper.createTestService
import org.junit.Test

class EmailAuthStepTest {
    @Test
    fun testPlayerIsCreatedWhenNotFound() {
        // setup
        val test = createTestService()

        // when
        val response = test.runPreAuth(emailAddress)

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(findPlayerByEmail(emailAddress)).isNotNull()
    }

    @Test
    fun testPlayerCanBeFound() {
        // setup
        val test = createTestService()

        // given
        test.createPlayer(emailAddress)
        test.setPreAuth { svc, _ -> EmailAuthStep(svc) }

        // when
        val response = test.runPreAuth(emailAddress)

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(findPlayerByEmail(emailAddress)).isNotNull()
    }

    @Test
    fun testDoesNotDuplicatePlayerRecords() {
        // setup
        val test = createTestService()
        test.createPlayer(emailAddress)
        test.setPreAuth { svc, _ -> EmailAuthStep(svc) }

        // given
        test.runPreAuth(emailAddress)

        // when
        val player = findPlayerByEmail(emailAddress)

        // then
        assertThat(player).isNotNull()
    }
}
