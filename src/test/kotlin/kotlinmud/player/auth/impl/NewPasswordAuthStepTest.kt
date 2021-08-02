package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import org.junit.Test

class NewPasswordAuthStepTest {
    private lateinit var player: PlayerDAO
    private val password = "foo"

    @Test
    fun testCanEnterNewPassword() {
        // given
        val test = setup()

        val response = test.runPreAuth(password)

        assertThat(player.password).isEqualTo(password)
        assertThat(response.message).isEqualTo("ok.")
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
