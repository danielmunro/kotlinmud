package kotlinmud.action.impl.admin.room

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomDescriptionAddTest {
    @Test
    fun testCanAddToRoomDescription() {
        // setup
        val test = createTestService()

        // given
        val room = test.getStartRoom()
        val descriptionAdd = "a long dirt road winds before you."

        // when
        test.runActionAsAdmin("room description add $descriptionAdd")

        // then
        assertThat(room.description).isEqualTo("tbd\n$descriptionAdd")
    }
}
