package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.mockk.confirmVerified
import io.mockk.verify
import kotlinmud.io.type.IOStatus
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
        verify {
            client.write(
"""
Spell Groups
============
                healing  8               illusion  8
            malediction  8            benediction  8
            enhancement  8

Skills
======
                   bash  6                berserk  8
           shield block  4                  parry  8
                  dodge  8                 disarm  6
                   trip  4              hamstring  6
        enhanced damage  8             meditation  8
          second attack  8                 dagger  4
                   mace  4                  sword  4
                  sword  4                   wand  4
                unarmed  4

Defaults
========
       warrior default  12          thief default  8
         cleric default  6           mage default  8


Current experience to level: 0
"""
        )
            client.write(any())
        }
        confirmVerified(client)

        // and
        assertThat(response.message).isEqualTo("ok.")
        assertThat(response.authStep).isInstanceOf(CustomizationAuthStep::class)
    }

    @Test
    fun testCanAdd() {
        // setup
        val test = setup()

        // when
        val response = test.runPreAuth("add trip")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testCannotAddTwice() {
        // setup
        val test = setup()

        // given
        test.runPreAuth("add trip")

        // when
        val response = test.runPreAuth("add trip")

        // then
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testAddingRemovesFromList() {
        // setup
        val test = setup()
        val client = test.getClient()

        // given
        test.runPreAuth("add trip")

        // when
        test.runPreAuth("list")

        // then
        verify { client.write(match { !it.contains("trip") }) }
        confirmVerified(client)
    }

    @Test
    fun testCanRemoveFromList() {
        // setup
        val test = setup()
        val client = test.getClient()

        // given
        test.runPreAuth("add trip")
        test.runPreAuth("remove trip")

        // when
        test.runPreAuth("list")

        // then
        verify {
            client.write(any())
            client.write(any())
            client.write(match { it.contains("trip") })
            client.write(any())
        }
        confirmVerified(client)
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
