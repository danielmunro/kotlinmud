package kotlinmud.mob.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class MobServiceTest {
    @Test
    fun testDecrementAffectsReducesTimeout() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val initial = 10

        // given
        mob.affects.add(createAffect(AffectType.BLESS, initial))

        // when
        testService.decrementAffects()

        // then
        val affect = mob.affects.find { it.type == AffectType.BLESS }!!
        assertThat(affect.timeout).isEqualTo(initial - 1)
    }

    @Test
    fun testAffectIsRemovedWhenTimeRunsOut() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val initial = 0

        // given
        mob.affects.add(createAffect(AffectType.BLESS, initial))

        // when
        testService.decrementAffects()

        // then
        assertThat(mob.affects).hasSize(0)
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
        val count = testService.findMobsInRoom().size

        // given
        mob1.disposition = Disposition.DEAD

        // when
        runBlocking { testService.pruneDeadMobs() }

        // then
        assertThat(testService.findMobsInRoom().size).isEqualTo(count - 1)
    }

    @Test
    fun testGuardAttackAggressor() {
        // setup
        val testService = createTestService()
        testService.createMob()
        val defender = testService.createMob()

        // given
        val guard = testService.createMobBuilder()
            .job(JobType.GUARD)
            .build()

        // when
        testService.runAction("kill ${getIdentifyingWord(defender)}")

        // then
        assertThat(testService.findFightForMob(guard)).isNotNull()
    }

    @Test
    fun testTrainSanityCheck() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob()
        mob.trains += 1
        val str = mob.calc(Attribute.STR)
        test.createMobBuilder()
            .job(JobType.TRAINER)
            .build()

        // when
        val response = test.runAction("train str")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you train your strength.")
        assertThat(mob.calc(Attribute.STR)).isEqualTo(str + 1)
        assertThat(mob.trains).isEqualTo(0)
    }

    @Test
    fun testTrainVitalSanityCheck() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob()
        val hp = mob.calc(Attribute.HP)
        mob.trains += 1
        test.createMobBuilder()
            .job(JobType.TRAINER)
            .build()

        // when
        val response = test.runAction("train hp")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you train your health.")
        assertThat(mob.calc(Attribute.HP)).isEqualTo(hp + 10)
        assertThat(mob.trains).isEqualTo(0)
    }

    @Test
    fun testPracticeSanityCheck() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob()
        mob.skills[SkillType.BASH] = 1
        mob.practices += 1
        test.createMobBuilder()
            .job(JobType.TRAINER)
            .build()

        // when
        val response = test.runAction("practice bash")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you practice bash.")
        assertThat(mob.skills[SkillType.BASH]!!).isGreaterThan(1)
        assertThat(mob.practices).isEqualTo(0)
    }
}
