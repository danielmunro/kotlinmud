package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class PasswordAuthStepTest {
    private lateinit var player: PlayerDAO
    private val password = "foo1234"

    @Test
    fun testCanEnterNewPassword() {
        // given
        val test = setup()

        // when
        val response = test.runPreAuth(password)

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testFailsWithWrongPassword() {
        // given
        val test = setup()

        // when
        val response = test.runPreAuth("yo")

        // then
        assertThat(response.status).isEqualTo(IOStatus.FAILED)
        assertThat(response.message).isEqualTo("password invalid")
    }

    private fun setup(): TestService {
        return createTestService().also {
            player = it.createPlayer(accountName)
            transaction { player.password = password }
            setPreAuth(it)
        }
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> PasswordAuthStep(authStepService, player) }
    }
}
