package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import org.junit.Test

class SpecializationAuthStepTest {
    @Test
    fun testCanChooseWarriorSpec() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("warrior")

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCanChooseWarriorPartialSpec() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("war")

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCanChooseThiefSpec() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("thief")

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCanChooseClericSpec() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("cleric")

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCanChooseMageSpec() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("mage")

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCannotChooseBadSpec() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("floodle")

        // then
        assertThat(response.message).isEqualTo("that is not a specialization.")
    }

    private fun setup(): TestService {
        return createTestService().also {
            it.createPlayer(accountName)
            setPreAuth(it)
        }
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> SpecializationAuthStep(authStepService, player) }
    }
}
