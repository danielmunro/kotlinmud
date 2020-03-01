package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.Disposition
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

        // then
        assertThat(response.message.toActionCreator).isEqualTo("A different test room\n" +
                "This is a different test room.\n" +
                "Exits [S]")
    }

    @Test
    fun testMobMovesSouth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "s")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("A back room\n" +
                "A back room is here.\n" +
                "Exits [N]")
    }

    @Test
    fun testMobCannotMoveInAnInvalidDirection() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "w")

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
}
