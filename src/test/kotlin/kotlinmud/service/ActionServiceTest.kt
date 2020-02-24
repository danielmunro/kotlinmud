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
    fun testMobCanGetItemFromRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)
        val item = room.inventory.items.first()

        // when
        val response = testService.runAction(mob, "get ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you pick up $item.")
        assertThat(mob.inventory.items).hasSize(1)
        assertThat(room.inventory.items).hasSize(0)
    }

    @Test
    fun testMobCannotGetNonexistentItemFromRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)

        // when
        val response = testService.runAction(mob, "get foo")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see that anywhere.")
        assertThat(mob.inventory.items).hasSize(0)
        assertThat(room.inventory.items).hasSize(1)
    }

    @Test
    fun testMobCanDropItem() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val item = testService.createItem(mob.inventory)
        val room = testService.getRoomForMob(mob)

        // when
        val response = testService.runAction(mob, "drop ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you drop $item.")
        assertThat(mob.inventory.items).hasSize(0)
        assertThat(room.inventory.items).hasSize(2)
    }

    @Test
    fun testMobCanBerserk() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BERSERK, 100))

        // when
        val response = testService.runActionForIOStatus(mob, "berserk", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Your pulse speeds up as you are consumed by rage!")
    }

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
