package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
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
        assertThat(response.message.toActionCreator).isEqualTo(describeRoom(Request(mob, "look", room), observers))
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
        assertThat(response.message.toActionCreator).isEqualTo("you can't see anything, you're blind!")
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
        assertThat(response.message.toActionCreator).isEqualTo("${item.name} is here.")
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
        assertThat(response.message.toActionCreator).isEqualTo("${item.name} is here.")
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
        assertThat(response.message.toActionCreator).isEqualTo("${mob2.name} is here.")
    }
}
