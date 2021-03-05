package kotlinmud.action.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import kotlin.test.Test

class ActionServiceTest {
    @Test
    fun testMobCanLoseConcentration() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        mob.skills[SkillType.BERSERK] = 1

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
        mob.skills[SkillType.BERSERK] = 1

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

        // given
        mob.skills[SkillType.BERSERK] = 100

        // when
        val response = testService.runActionForIOStatus(mob, "berserk", IOStatus.OK)

        // then
        assertThat(response.delay).isGreaterThan(0)
    }

    @Test
    fun testMobCanTargetMobInRoomWithAbility() {
        // setup
        val testService = createTestService()
        val target = testService.createMob()
        val mob = testService.createMob()

        // given
        mob.skills[SkillType.BITE] = 100

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
        val target = testService.createMob()
        val mob = testService.createMob {
            it.skills[SkillType.BITE] = 100
        }

        // given
        testService.addFight(mob, target)

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
        val target = testService.createMob()
        val mob = testService.createMob {
            it.skills[SkillType.BITE] = 100
        }

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
        val target = testService.createMob()
        val mob = testService.createMob {
            it.skills[SkillType.BITE] = 100
        }

        // given
        testService.runActionForIOStatus(mob, "bite ${getIdentifyingWord(target)}", IOStatus.OK)

        // when
        val rounds = testService.proceedFights()

        // then
        assertThat(rounds).hasSize(1)
    }

    @Test
    fun testMobCanCastInvisibilityOnTarget() {
        // setup
        val testService = createTestService()

        // given
        testService.createMob {
            it.skills[SkillType.INVISIBILITY] = 100
        }
        val target = testService.createMob()

        // when
        val response = testService.runAction("cast invis ${getIdentifyingWord(target)}")

        // expect
        assertThat(response.message.toActionCreator).isEqualTo("$target fades out of existence.")
    }

    @Test
    fun testMobCanCastInvisibilityOnSelf() {
        // setup
        val testService = createTestService()

        // given
        val mob = testService.createMob {
            it.skills[SkillType.INVISIBILITY] = 100
        }

        // when
        val response = testService.runAction("cast invis")

        // expect
        assertThat(response.message.toActionCreator).isEqualTo("$mob fades out of existence.")
    }

    @Test
    fun testMobCanCastInvisibilityOnItemInInventory() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob {
            it.skills[SkillType.INVISIBILITY] = 100
        }

        // given
        val item = testService.createItem()
        mob.items.add(item)

        // when
        val response = testService.runAction("cast invis ${getIdentifyingWord(item)}")

        // expect
        assertThat(response.message.toActionCreator).isEqualTo("$item fades out of existence.")
    }

    @Test
    fun testEmptyInputDoesNotCrash() {
        // setup
        val test = createTestService()

        // when
        val response = test.runAction("")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("")
    }

    @Test
    fun testUnknownInputGetsAResponse() {
        // setup
        val test = createTestService()

        // when
        val response = test.runAction("floodle")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("what was that?")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }
}
