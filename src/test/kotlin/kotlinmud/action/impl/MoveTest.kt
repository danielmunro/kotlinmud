package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.action.impl.info.describeRoom
import kotlinmud.mob.type.Disposition
import kotlinmud.test.createTestService
import org.junit.Test

class MoveTest {
    @Test
    fun testMobMovesNorth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "n")
        val room = testService.getRoomForMob(mob)

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(room, mob, testService.getMobsForRoom(room), testService.getItemsFor(room))
        )
    }

    @Test
    fun testMobMovesSouth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "s")
        val room = testService.getRoomForMob(mob)

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(room, mob, testService.getMobsForRoom(room), testService.getItemsFor(room))
        )
    }

    @Test
    fun testMobCannotMoveInAnInvalidDirection() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "u")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Alas, that direction does not exist.")
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
        assertThat(response.message.toActionCreator).isEqualTo("you are sitting and cannot do that.")
    }

    @Test
    fun testMobCannotMoveDirectionIfDoorIsClosed() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "e")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you must open the door first.")
    }

    @Test
    fun testMobCannotMoveOverGreatElevations() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val room = test.getRooms().find { it.id == 119 }!!

        // given
        test.putMobInRoom(mob, room)

        // when
        val response = test.runAction(mob, "east")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't climb that elevation.")
    }
}
