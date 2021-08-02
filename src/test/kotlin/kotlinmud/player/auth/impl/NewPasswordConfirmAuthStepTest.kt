package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class NewPasswordConfirmAuthStepTest {
    private lateinit var player: PlayerDAO
    private val password = "foo"

    @Test
    fun testCanConfirmNewPassword() {
        // given
        val test = setup()

        // when
        val response = test.runPreAuth(password)

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testRejectsNonmatchingNewPassword() {
        // given
        val test = setup()

        val response = test.runPreAuth("thouneohcuoheuo")

        assertThat(player.password).isEqualTo(password)
        assertThat(response.message).isEqualTo("ok.")
    }

    private fun setup(): TestService {
        return createTestService().also {
            player = it.createPlayer(accountName)
            transaction { player.password = password }
            setPreAuth(it)
        }
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> NewPasswordConfirmAuthStep(authStepService, player) }
    }
}
