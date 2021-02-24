package kotlinmud.action.impl.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.repository.findFightForMob
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

        // given
        testService.addFight(mob, target)

        // when
        testService.runActionForIOStatus(mob, "flee", IOStatus.OK)

        // then
        testService.findFightForMob(mob).let { assertThat(it!!.isOver()).isEqualTo(true) }
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
