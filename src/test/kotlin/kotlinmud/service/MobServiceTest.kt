package kotlinmud.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.test.createTestService

class MobServiceTest {
    @Test
    fun testDecrementAffectsReducesTimeout() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val initial = 10

        // given
        mob.affects.add(AffectInstance(AffectType.BLESS, initial))

        // when
        testService.decrementAffects()

        // then
        assertThat(mob.affects.first().timeout).isEqualTo(initial - 1)
    }

    @Test
    fun testAffectIsRemovedWhenTimeRunsOut() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val initial = 0

        // given
        mob.affects.add(AffectInstance(AffectType.BLESS, initial))

        // when
        testService.decrementAffects()

        // then
        assertThat(mob.affects).hasSize(0)
    }

    @Test
    fun testRespawnRecreatesMobs() {
        // setup
        val testService = createTestService()
        val room = testService.getStartRoom()
        val resetNumbers = 1

        // when
        testService.respawnWorld()

        // then
        assertThat(testService.getMobsForRoom(room)).hasSize(resetNumbers)
    }
}
