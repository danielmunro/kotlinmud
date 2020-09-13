package kotlinmud.action.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import kotlin.test.Test
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.factory.createSkill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction

class ActionServiceTest {
    @Test
    fun testMobCanLoseConcentration() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        createSkill(SkillType.BERSERK, mob)

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
        createSkill(SkillType.BERSERK, mob)

        // given
        transaction { mob.mv = 0 }

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
        createSkill(SkillType.BERSERK, mob, 100)

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
        createSkill(SkillType.BITE, mob, 100)

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
        val mob = testService.createMob()
        createSkill(SkillType.BITE, mob, 100)

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
        val mob = testService.createMob()
        createSkill(SkillType.BITE, mob, 100)

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
        val testService = createTestServiceWithResetDB()
        val target = testService.createMob()
        val mob = testService.createMob()
        createSkill(SkillType.BITE, mob, 100)

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
            createSkill(SkillType.INVISIBILITY, it, 100)
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
            createSkill(SkillType.INVISIBILITY, it, 100)
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
        val mob = testService.createMob()

        // given
        createSkill(SkillType.INVISIBILITY, mob, 100)
        val item = testService.createItem()
        transaction { item.mobInventory = mob }

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
