package kotlinmud.action.impl.admin.room

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomDescriptionListTest {
    @Test
    fun testCanListRoomDescription() {
        // setup
        val test = createTestService()

        val room = test.getStartRoom()
        room.description = """This room has a terribly long description,
in fact, it reaches over several
lines."""
        val response = test.runActionAsAdmin("room description list")

        assertThat(response.message.toActionCreator).isEqualTo(
            """#1 This room has a terribly long description,
#2 in fact, it reaches over several
#3 lines."""
        )
    }
}
