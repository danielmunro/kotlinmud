package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import org.junit.Test

class NewPasswordAuthStepTest {
    private lateinit var player: PlayerDAO
    private val password = "foo1234"
    private val tooShortPassword = "foo"

    @Test
    fun testCanEnterNewPassword() {
        // given
        val test = setup()

        // when
        val response = test.runPreAuth(password)

        // then
        assertThat(player.password).isEqualTo(password)
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testPasswordMustBeAtLeast7Characters() {
        // given
        val test = setup()

        // when
        val response = test.runPreAuth(tooShortPassword)

        // then
        assertThat(response.message).isEqualTo("That password is either too short or too simple.")
    }

    private fun setup(): TestService {
        return createTestService().also {
            player = it.createPlayer(accountName)
            setPreAuth(it)
        }
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> NewPasswordAuthStep(authStepService, player) }
    }
}
