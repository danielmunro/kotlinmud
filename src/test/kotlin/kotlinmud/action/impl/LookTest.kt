package kotlinmud.action.impl

import kotlin.test.assertEquals
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.io.Request
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class LookTest {
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
    fun testCannotSeeRoomWhenBlind() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        mob.affects.add(AffectInstance(AffectType.BLIND, 1))

        // when
        val response = testService.runAction(mob, "look")

        // then
        assertEquals(
            "you can't see anything, you're blind!",
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
}
