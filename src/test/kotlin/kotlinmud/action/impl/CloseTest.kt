package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.IOStatus
import kotlinmud.room.exit.DoorDisposition
import kotlinmud.test.createTestService
import org.junit.Test

class CloseTest {
    @Test
    fun testCanCloseDoors() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        testService.getStartRoom().exits.find { it.door != null }?.let { it.door?.disposition = DoorDisposition.OPEN }

        // when
        val response = testService.runAction(mob, "close door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you close a heavy wooden door.")
    }

    @Test
    fun testCannotCloseAClosedDoor() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        testService.getStartRoom().exits.find { it.door != null }?.let { it.door?.disposition = DoorDisposition.CLOSED }

        // when
        val response = testService.runAction(mob, "close door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("it is already closed.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testCannotCloseDoorsThatDoNotExist() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        testService.getStartRoom().exits.find { it.door != null }?.let { it.door?.disposition = DoorDisposition.OPEN }

        // when
        val response = testService.runAction(mob, "close grate")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see that anywhere.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }
}
