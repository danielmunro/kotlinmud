package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinmud.mob.race.impl.Human
import kotlinmud.player.auth.model.CreationFunnel
import kotlinmud.test.TestService
import kotlinmud.test.createTestServiceWithResetDB
import org.junit.Test

class CustomizationAuthStepTest {
    @Test
    fun testCanCompleteCustomization() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("done")

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(CompleteAuthStep::class)
    }

    private fun setup(): TestService {
        return createTestServiceWithResetDB().also {
            it.createPlayer(emailAddress)
            setPreAuth(it)
        }
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player ->
            val funnel = CreationFunnel(player.email)
            funnel.mobName = "foo"
            funnel.mobRace = Human()
            funnel.mobRoom = test.getStartRoom()
            authStepService.addCreationFunnel(funnel)
            CustomizationAuthStep(authStepService, player)
        }
    }
}
