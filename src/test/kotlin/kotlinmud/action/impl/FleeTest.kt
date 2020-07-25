package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.fight.Fight
import kotlinmud.test.createTestService
import org.junit.Test

class FleeTest {
    @Test
    fun testFleeEndsFight() {
        // setup
        val testService = createTestService()
        val room = testService.getStartRoom()
        testService.createRoom { room.north = it }
        val mob = testService.createMob()
        val target = testService.createMob()
        val fight = Fight(mob, target)

        // given
        testService.addFight(fight)

        // when
        testService.runActionForIOStatus(mob, "flee", IOStatus.OK)

        // then
        assertThat(fight.isOver()).isEqualTo(true)
    }

    @Test
    fun testFleeRequiresFightingDisposition() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runActionForIOStatus(mob, "flee", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you are standing and cannot do that.")
    }
}
