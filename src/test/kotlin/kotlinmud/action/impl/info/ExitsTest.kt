package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Door
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.helper.createTestService
import org.junit.Test

class ExitsTest {
    @Test
    fun testCanSeeExits() {
        // setup
        val test = createTestService()

        val room1 = test.getStartRoom()
        val room2 = test.createRoom()
        val room3 = test.createRoom()
        val room4 = test.createRoom()

        connect(room1).toRoom(
            listOf(
                Pair(room2, Direction.NORTH),
                Pair(room3, Direction.DOWN),
                Pair(room4, Direction.SOUTH),
            )
        )

        room1.northDoor = Door(
            "a heavy iron door",
            "a big heavy iron door",
            "TBD",
            DoorDisposition.CLOSED,
            1,
        )

        val response = test.runAction("exits")

        assertThat(response.message.toActionCreator).isEqualTo("Exits include:\n\nnorth - a heavy iron door\nsouth - a test room\ndown - a test room")
    }
}
