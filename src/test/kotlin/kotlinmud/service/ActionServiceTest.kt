package kotlinmud.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinmud.action.actions.describeRoom
import kotlinmud.io.IOStatus
import kotlinmud.io.Request
import kotlinmud.mob.Disposition
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.skill.SkillType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import kotlin.test.assertNotNull

class ActionServiceTest {
    @Test
    fun testLookDescribesARoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)
        val observers = testService.getMobsForRoom(room).filter { it != mob }

        // when
        val response = testService.runAction(mob, "look")

        // then
        assertEquals(
            describeRoom(Request(mob, "look", room), observers),
            response.message.toActionCreator)
    }

    @Test
    fun testCanLookAtItemInRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)
        val item = room.inventory.items.first()

        // when
        val response = testService.runAction(mob, "look ${getIdentifyingWord(item)}")

        // then
        assertEquals(
            "${item.name} is here.",
            response.message.toActionCreator)
    }

    @Test
    fun testCanLookAtItemInInventory() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val item = testService.createItem(mob.inventory)

        // when
        val response = testService.runAction(mob, "look ${getIdentifyingWord(item)}")

        // then
        assertEquals(
            "${item.name} is here.",
            response.message.toActionCreator)
    }

    @Test
    fun testCanLookAtMobInRoom() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val mob2 = testService.createMob()

        // when
        val response = testService.runAction(mob1, "look ${mob2.name.split(" ")[0]}")

        // then
        assertEquals(
            "${mob2.name} is here.",
            response.message.toActionCreator)
    }

    @Test
    fun testMobMovesNorth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "n")

        // then
        assertEquals("test room no. 2\n" +
                "a test room is here\n" +
                "Exits [S]", response.message.toActionCreator)
    }

    @Test
    fun testMobMovesSouth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "s")

        // then
        assertEquals("test room no. 3\n" +
                "a test room is here\n" +
                "Exits [N]", response.message.toActionCreator)
    }

    @Test
    fun testMobCannotMoveInAnInvalidDirection() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "w")

        // then
        assertEquals("Alas, that direction does not exist.", response.message.toActionCreator)
    }

    @Test
    fun testMobCannotMoveWhileSitting() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        mob.disposition = Disposition.SITTING

        // when
        val response = testService.runAction(mob, "n")

        // then
        assertEquals("you are sitting and cannot do that.", response.message.toActionCreator)
    }

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
        assertTrue(response.message.toActionCreator.startsWith("you pick up the"))
        assertEquals(1, mob.inventory.items.count())
    }

    @Test
    fun testMobCannotGetNonexistentItemFromRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "get foo")

        // then
        assertTrue(response.message.toActionCreator == "you don't see that anywhere.", response.message.toActionCreator)
        assertEquals(0, mob.inventory.items.count())
    }

    @Test
    fun testMobCanDropItem() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val item = testService.createItem(mob.inventory)

        // when
        val response = testService.runAction(mob, "drop ${getIdentifyingWord(item)}")

        // then
        assertTrue(response.message.toActionCreator.startsWith("you drop the "))
        assertEquals(0, mob.inventory.items.count())
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
        assertEquals("Your pulse speeds up as you are consumed by rage!", response.message.toActionCreator)
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
        assertEquals("You lost your concentration.", response.message.toActionCreator)
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
        assertEquals("You are too tired", response.message.toActionCreator)
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
        assertTrue(mob.delay > 0)
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
        assertEquals("You bite $target.", response.message.toActionCreator)
        assertEquals("$mob bites you.", response.message.toTarget)
        assertEquals("$mob bites $target.", response.message.toObservers)
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
        assertEquals("You bite $target.", response.message.toActionCreator)
        assertEquals("$mob bites you.", response.message.toTarget)
        assertEquals("$mob bites $target.", response.message.toObservers)
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
        assertNotNull(fight)
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
        assertEquals(1, rounds.size)
    }
}
