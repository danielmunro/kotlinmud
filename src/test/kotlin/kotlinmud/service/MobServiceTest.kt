package kotlinmud.service

import kotlin.test.Test
import kotlin.test.assertEquals
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
        assertEquals(initial - 1, mob.affects.first().timeout)
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
        assertEquals(0, mob.affects.size)
    }
}
