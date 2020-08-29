package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.mockk.confirmVerified
import io.mockk.verify
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

    @Test
    fun testCanList() {
        // setup
        val test = setup()
        val client = test.getClient()

        // when
        val response = test.runPreAuth("list")

        // then
        verify { client.write(
"""
Spell Groups
============
                healing  8               illusion  8
            malediction  8

Skills
======
                   bash  6                berserk  8
                  dodge  8                  parry  8
           shield block  4

Defaults
========
       warrior default  12          thief default  8
         cleric default  6                   mage  8


Current experience to level: 0
"""
        ) }
        confirmVerified()

        // and
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(CustomizationAuthStep::class)
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
