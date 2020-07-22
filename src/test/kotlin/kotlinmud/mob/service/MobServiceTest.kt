package kotlinmud.mob.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlin.test.Test
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MobServiceTest {
    @Test
    fun testDecrementAffectsReducesTimeout() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val initial = 10

        // given
        transaction { createAffect(AffectType.BLESS, initial).mob = mob }

        // when
        testService.decrementAffects()

        // then
        val affect = transaction { mob.affects.find { it.type == AffectType.BLESS } }!!
        assertThat(affect.timeout).isEqualTo(initial - 1)
    }

    @Test
    fun testAffectIsRemovedWhenTimeRunsOut() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val initial = 0

        // given
        transaction { createAffect(AffectType.BLESS, initial).mob = mob }

        // when
        testService.decrementAffects()

        // then
        assertThat(transaction { mob.affects.toList() }).hasSize(0)
    }

    @Test
    fun testDecrementDelayForMobs() {
        // setup
        val testService = createTestService()
        val client1 = testService.createClient()
        val client2 = testService.createClient()
        val client3 = testService.createClient()

        // given
        client1.delay = 0
        client2.delay = 3
        client3.delay = 1

        // when
        testService.decrementDelays()

        // then
        assertThat(client1.delay).isEqualTo(0)
        assertThat(client2.delay).isEqualTo(2)
        assertThat(client3.delay).isEqualTo(0)
    }

    @Test
    fun testPruneDeadMobs() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val mobCount = transaction { Mobs.selectAll().count() }

        // given
        transaction { mob1.disposition = Disposition.DEAD }

        // when
        testService.pruneDeadMobs()

        // then
        assertThat(transaction { Mobs.selectAll().count() }).isEqualTo(mobCount - 1)
    }

    @Test
    fun testGuardsAttackAggressors() {
        // setup
        val testService = createTestService()
        val aggressor = testService.createMob()
        val defender = testService.createMob()
        val guard = testService.createMob()

        // given
        transaction { guard.job = JobType.GUARD }

        // when
        testService.runAction(aggressor, "kill ${getIdentifyingWord(defender)}")

        // then
        assertThat(testService.findFightForMob(guard)).isNotNull()
    }
}
