package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.helper.createTestService
import org.junit.Test

class CloseTest {
    @Test
    fun testCanCloseDoors() {
        // setup
        val testService = createTestService()

        // given
        val name = "a door"
        val room = testService.getStartRoom()
        val door = testService.createDoor()
        door.disposition = DoorDisposition.OPEN
        room.northDoor = door

        // when
        val response = testService.runAction("close door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you close $name.")
    }

    @Test
    fun testCannotCloseAClosedDoor() {
        // setup
        val testService = createTestService()

        // given
        val room = testService.getStartRoom()
        room.northDoor = testService.createDoor()
        room.northDoor!!.disposition = DoorDisposition.CLOSED

        // when
        val response = testService.runAction("close door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("it is already closed.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testCannotCloseDoorsThatDoNotExist() {
        // setup
        val testService = createTestService()

        // when
        val response = testService.runAction("close grate")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see that anywhere.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }
}
