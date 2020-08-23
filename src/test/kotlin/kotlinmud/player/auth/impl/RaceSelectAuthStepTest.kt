package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinmud.test.TestService
import kotlinmud.test.createTestService
import org.junit.Test

class RaceSelectAuthStepTest {
    @Test
    fun testCanSelectAPlayableRace() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("human")

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(SpecializationAuthStep::class)
    }

    @Test
    fun testCannotSelectANonPlayableRace() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("horse")

        // then
        assertThat(response.message).isEqualTo("that is not a race. Enter 'help race' for help.")
        assertThat(response.authStep).isInstanceOf(RaceSelectAuthStep::class)
    }

    @Test
    fun testBadInputHandledCorrectly() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("foo")

        // then
        assertThat(response.message).isEqualTo("that is not a race. Enter 'help race' for help.")
    }

    private fun setup(): TestService {
        return createTestService().also {
            it.createPlayer(emailAddress)
            setPreAuth(it)
        }
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> RaceSelectAuthStep(authStepService, player) }
    }
}