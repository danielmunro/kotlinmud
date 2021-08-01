package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.player.repository.findPlayerByName
import kotlinmud.test.helper.createTestService
import org.junit.Test

class EmailAuthStepTest {
    @Test
    fun testPlayerIsCreatedWhenNotFound() {
        // setup
        val test = createTestService()

        // when
        val response = test.runPreAuth(accountName)

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(findPlayerByEmail(accountName)).isNotNull()
    }

    @Test
    fun testPlayerCanBeFound() {
        // setup
        val test = createTestService()

        // given
        test.createPlayer(accountName)

        // when
        val response = test.runPreAuth(accountName)

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(findPlayerByName(accountName)).isNotNull()
    }

    @Test
    fun testDoesNotDuplicatePlayerRecords() {
        // setup
        val test = createTestService()
        test.createPlayer(accountName)
        test.setPreAuth { svc, _ -> AccountNameAuthStep(svc) }

        // given
        test.runPreAuth(accountName)

        // when
        val player = findPlayerByName(accountName)

        // then
        assertThat(player).isNotNull()
    }
}
