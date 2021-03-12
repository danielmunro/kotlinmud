package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import org.junit.Test

class GenderSelectAuthStepTest {
    @Test
    fun testCanSelectDefaultGender() {
        // setup
        val test = createTestService()
        test.createPlayer("foo@bar.com")
        setPreAuth(test)

        val response = test.runPreAuth("")

        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCanSelectAllGender() {
        // setup
        val test = createTestService()
        test.createPlayer("foo@bar.com")
        setPreAuth(test)

        val response = test.runPreAuth("any")

        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCanSelectAllGenderWithPartialMatch() {
        // setup
        val test = createTestService()
        test.createPlayer("foo@bar.com")
        setPreAuth(test)

        val response = test.runPreAuth("a")

        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCanHandleBadInput() {
        // setup
        val test = createTestService()
        test.createPlayer("foo@bar.com")
        setPreAuth(test)

        val response = test.runPreAuth("floobar")

        assertThat(response.message).isEqualTo("please choose from the list (default: none)")
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> GenderSelectAuthStep(authStepService, player) }
    }
}
