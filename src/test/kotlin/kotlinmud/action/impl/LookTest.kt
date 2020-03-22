package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
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
        assertThat(response.message.toActionCreator).isEqualTo(describeRoom(room, mob, observers))
    }

    @Test
    fun testCannotSeeRoomWhenBlind() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        mob.affectsService().add(AffectInstance(AffectType.BLIND, 1))

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
        assertThat(response.message.toActionCreator).isEqualTo(item.description)
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
        assertThat(response.message.toActionCreator).isEqualTo(item.description)
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
        assertThat(response.message.toActionCreator).isEqualTo(mob2.description)
    }

    @Test
    fun testCanSeeADoor() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)
        val door = room.exits.find { it.door != null }!!.door!!

        // when
        val response = testService.runAction(mob, "look")

        // then
        assertThat(response.message.toActionCreator).contains(door.name)
    }

    @Test
    fun testCannotSeeInvisibleMobs() {
        // setup
        val testService = createTestService()

        // given
        val mob1 = testService.buildMob(
            testService.mobBuilder()
                .addAffect(AffectType.INVISIBLE)
        )
        val mob2 = testService.createMob()

        // when
        val response = testService.runAction(mob2, "look")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(mob1.name)
    }
}
