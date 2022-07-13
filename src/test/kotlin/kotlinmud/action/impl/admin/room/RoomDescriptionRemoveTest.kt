package kotlinmud.action.impl.admin.room

import kotlinmud.test.helper.createTestService
import org.junit.Test
import kotlin.test.assertEquals

class RoomDescriptionRemoveTest {
    @Test
    fun testThatADescriptionCanChange() {
        // setup
        val test = createTestService()
        val room = test.getStartRoom()
        val model = test.getStartRoomModel()

        // given
        room.description = """Line 1
2
3
4"""

        // when
        test.runActionAsAdmin("room description remove 3")

        // then
        assertEquals(
            """Line 1
2
4""",
            room.description
        )

        // and
        assertEquals(
            """Line 1
2
4""",
            model.description
        )
    }
}
