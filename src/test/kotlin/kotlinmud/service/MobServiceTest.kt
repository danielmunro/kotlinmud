package kotlinmud.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.mob.Disposition
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

    @Test
    fun testDecrementDelayForMobs() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val mob2 = testService.createMob()
        val mob3 = testService.createMob()

        // given
        mob1.delay = 0
        mob2.delay = 3
        mob3.delay = 1

        // when
        testService.decrementDelays()

        // then
        assertThat(mob1.delay).isEqualTo(0)
        assertThat(mob2.delay).isEqualTo(2)
        assertThat(mob3.delay).isEqualTo(0)
    }

    @Test
    fun testPruneDeadMobs() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val mobCount = testService.getMobRooms().size

        // given
        mob1.disposition = Disposition.DEAD

        // when
        testService.pruneDeadMobs()

        // then
        assertThat(testService.getMobRooms()).hasSize(mobCount - 1)
    }
}
