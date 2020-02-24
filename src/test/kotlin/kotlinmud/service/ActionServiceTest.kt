package kotlinmud.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import kotlin.test.Test
import kotlinmud.io.IOStatus
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.skill.SkillType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord

class ActionServiceTest {
    @Test
    fun testMobCanLoseConcentration() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BERSERK, 1))

        // when
        val response = testService.runActionForIOStatus(mob, "berserk", IOStatus.FAILED)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You lost your concentration.")
    }

    @Test
    fun testMobMustPayCostToUseSkill() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BERSERK, 100))

        // given
        mob.mv = 0

        // when
        val response = testService.runActionForIOStatus(mob, "berserk", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You are too tired")
    }

    @Test
    fun testMobAppliesDelayWhenUsingSkill() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BERSERK, 100))

        // when
        testService.runActionForIOStatus(mob, "berserk", IOStatus.OK)

        // then
        assertThat(mob.delay).isGreaterThan(0)
    }

    @Test
    fun testMobCanTargetMobInRoomWithAbility() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val target = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BITE, 100))

        // when
        val response = testService.runActionForIOStatus(mob, "bite ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You bite $target.")
        assertThat(response.message.toTarget).isEqualTo("$mob bites you.")
        assertThat(response.message.toObservers).isEqualTo("$mob bites $target.")
    }

    @Test
    fun testMobCanTargetMobInFightWithAbility() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val target = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BITE, 100))

        // given
        testService.addFight(Fight(mob, target))

        // when
        val response = testService.runActionForIOStatus(mob, "bite", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You bite $target.")
        assertThat(response.message.toTarget).isEqualTo("$mob bites you.")
        assertThat(response.message.toObservers).isEqualTo("$mob bites $target.")
    }

    @Test
    fun testOffensiveSkillTriggersFight() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val target = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BITE, 100))

        // given
        testService.runActionForIOStatus(mob, "bite ${getIdentifyingWord(target)}", IOStatus.OK)

        // when
        val fight = testService.findFightForMob(mob)

        // then
        assertThat(fight).isNotNull()
    }

    @Test
    fun testOffensiveSkillTriggersOneAndOnlyOneFight() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val target = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BITE, 100))

        // given
        testService.runActionForIOStatus(mob, "bite ${getIdentifyingWord(target)}", IOStatus.OK)
        testService.runActionForIOStatus(mob, "bite ${getIdentifyingWord(target)}", IOStatus.OK)

        // when
        val rounds = testService.proceedFights()

        // then
        assertThat(rounds).hasSize(1)
    }
}
