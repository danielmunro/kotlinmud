package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class CloseTest {
    @Test
    fun testCanCloseDoors() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        val name = "a door"
        val room = testService.getStartRoom()
        transaction {
            room.northDoor = DoorDAO.new {
                this.name = name
                description = "a door"
                disposition = DoorDisposition.OPEN
                defaultDisposition = DoorDisposition.OPEN
            }
        }

        // when
        val response = testService.runAction(mob, "close door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you close $name.")
    }

    @Test
    fun testCannotCloseAClosedDoor() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        val room = testService.getStartRoom()
        transaction {
            room.northDoor = DoorDAO.new {
                name = "a door"
                description = "a door"
                disposition = DoorDisposition.CLOSED
                defaultDisposition = DoorDisposition.CLOSED
            }
        }

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

        // when
        val response = testService.runAction(mob, "close grate")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see that anywhere.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }
}
