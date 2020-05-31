package kotlinmud.mob.service

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNotNull
import kotlin.test.Test
import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType
import kotlinmud.mob.model.MobBuilder
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord

class MobServiceTest {
    @Test
    fun testDecrementAffectsReducesTimeout() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val initial = 10

        // given
        mob.affects().add(AffectInstance(AffectType.BLESS, initial))

        // when
        testService.decrementAffects()

        // then
        val affect = mob.affects().findByType(AffectType.BLESS)!!
        assertThat(affect.timeout).isEqualTo(initial - 1)
    }

    @Test
    fun testAffectIsRemovedWhenTimeRunsOut() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val initial = 0

        // given
        mob.affects().add(AffectInstance(AffectType.BLESS, initial))

        // when
        testService.decrementAffects()

        // then
        assertThat(mob.affects().getAffects()).hasSize(0)
    }

    @Test
    fun testRespawnRecreatesMobs() {
        // setup
        val testService = createTestService()
        val room = testService.getStartRoom()
        val resetNumbers = 2

        // when
        testService.respawnWorld()

        // then
        assertThat(testService.getMobsForRoom(room)).hasSize(resetNumbers)
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
        val mobCount = testService.getMobRooms().size

        // given
        mob1.disposition = Disposition.DEAD

        // when
        testService.pruneDeadMobs()

        // then
        assertThat(testService.getMobRooms()).hasSize(mobCount - 1)
    }

    @Test
    fun testGuardsAttackAggressors() {
        // setup
        val testService = createTestService()
        val aggressor = testService.createMob()
        val defender = testService.createMob()

        // given
        val guard = testService.createMob(JobType.GUARD)

        // when
        testService.runAction(aggressor, "kill ${getIdentifyingWord(defender)}")

        // then
        assertThat(testService.findFightForMob(guard)).isNotNull()
    }

    @Test
    fun testMobMustBeInRoomToCreateNewRoom() {
        // setup
        val test = createTestService()

        // when
        val mob = MobBuilder()
            .id(0)
            .name("foo")
            .hp(0)
            .mana(0)
            .mv(0)
            .race(Human())
            .build()

        // then
        assertThat { test.addNewRoom(mob) }.isFailure().hasMessage("mob must be in a room to add a room")
    }
}
