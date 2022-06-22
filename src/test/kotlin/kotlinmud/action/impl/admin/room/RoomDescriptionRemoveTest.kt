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
        room.description = """Line 1
2
3
4"""

        test.runActionAsAdmin("room description remove 3")

        assertEquals(
            """Line 1
2
4""",
            room.description
        )
    }
}
