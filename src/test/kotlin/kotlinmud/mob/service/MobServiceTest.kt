package kotlinmud.mob.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import kotlin.test.Test
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.select
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

        // given
        transaction { mob1.disposition = Disposition.DEAD }

        // when
        testService.pruneDeadMobs()

        // then
        assertThat(transaction { Mobs.select { Mobs.id eq mob1.id }.count() }).isEqualTo(0)
    }

    @Test
    fun testGuardsAttackAggressors() {
        // setup
        val testService = createTestService()
        testService.createMob()
        val defender = testService.createMob()
        val guard = testService.createMob()

        // given
        transaction { guard.job = JobType.GUARD }

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
        val str = mob.calc(Attribute.STR)
        transaction { mob.mobCard?.trains = 1 }
        test.createMob { it.job = JobType.TRAINER }

        // when
        val response = test.runAction("train str")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you train your strength.")
        assertThat(mob.calc(Attribute.STR)).isEqualTo(str + 1)
        assertThat(transaction { mob.mobCard!!.trains }).isEqualTo(0)
    }

    @Test
    fun testTrainVitalSanityCheck() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob()
        val hp = mob.calc(Attribute.HP)
        transaction { mob.mobCard?.trains = 1 }
        test.createMob { it.job = JobType.TRAINER }

        // when
        val response = test.runAction("train hp")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you train your health.")
        assertThat(mob.calc(Attribute.HP)).isEqualTo(hp + 10)
        assertThat(transaction { mob.mobCard!!.trains }).isEqualTo(0)
    }

    @Test
    fun testPracticeSanityCheck() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createPlayerMob()
        val skill = transaction { SkillDAO.new {
            this.mob = mob
            type = SkillType.BASH
            level = 1
        } }
        val level = transaction { skill.level }
        transaction { mob.mobCard?.practices = 1 }
        test.createMob { it.job = JobType.TRAINER }

        // when
        val response = test.runAction("practice bash")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you practice bash.")
        assertThat(transaction { mob.skills.find { it.type == SkillType.BASH } }!!.level).isGreaterThan(level)
        assertThat(transaction { mob.mobCard!!.practices }).isEqualTo(0)
    }
}
