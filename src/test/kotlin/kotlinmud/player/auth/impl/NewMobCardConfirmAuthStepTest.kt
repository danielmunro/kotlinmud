package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import kotlinx.coroutines.runBlocking
import org.junit.Test

const val name = "foo"

class NewMobCardConfirmAuthStepTest {
    @Test
    fun testCanConfirm() {
        // setup
        val test = setup()

        // when
        val response = runBlocking { test.runPreAuth("yes") }

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(RaceSelectAuthStep::class)
    }

    @Test
    fun testCanDeny() {
        // setup
        val test = setup()

        // when
        val response = runBlocking { test.runPreAuth("no") }

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(MobSelectAuthStep::class)
    }

    @Test
    fun testCanConfirmPartial() {
        // setup
        val test = setup()

        // when
        val response = runBlocking { test.runPreAuth("y") }

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCanDenyPartial() {
        // setup
        val test = setup()

        // when
        val response = runBlocking { test.runPreAuth("n") }

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testBadInput() {
        // setup
        val test = setup()

        // when
        val response = runBlocking { test.runPreAuth("foo") }

        // then
        assertThat(response.message).isEqualTo("Please answer yes or no (y/n):")
    }

    private fun setup(): TestService {
        return createTestService().also {
            it.createPlayer(emailAddress)
            setPreAuth(it)
        }
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> NewMobCardConfirmAuthStep(authStepService, player, name) }
    }
}
