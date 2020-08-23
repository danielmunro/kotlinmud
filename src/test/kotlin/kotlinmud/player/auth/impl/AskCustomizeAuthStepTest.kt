package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinmud.test.TestService
import kotlinmud.test.createTestServiceWithResetDB
import org.junit.Test

class AskCustomizeAuthStepTest {
    @Test
    fun testCanConfirm() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("yes")

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(CustomizeAuthStep::class)
    }

    @Test
    fun testCanConfirmPartial() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("y")

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(CustomizeAuthStep::class)
    }

    @Test
    fun testCanDeny() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("no")

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(CompleteAuthStep::class)
    }

    @Test
    fun testCanDenyPartial() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("n")

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(CompleteAuthStep::class)
    }

    @Test
    fun testCanHandleBadInput() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("foo")

        // then
        assertThat(response.message).isEqualTo("please answer 'yes' or 'no'")
        assertThat(response.authStep).isInstanceOf(AskCustomizeAuthStep::class)
    }

    private fun setup(): TestService {
        return createTestServiceWithResetDB().also {
            it.createPlayer(emailAddress)
            setPreAuth(it)
        }
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> AskCustomizeAuthStep(authStepService, player) }
    }
}
