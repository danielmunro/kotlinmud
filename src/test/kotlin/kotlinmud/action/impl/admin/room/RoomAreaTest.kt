package kotlinmud.action.impl.admin.room

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomAreaTest {
    @Test
    fun testCanChangeRoomArea() {
        // setup
        val test = createTestService()

        // given
        val room = test.getStartRoom()

        // when
        test.runActionAsAdmin("room area Test")

        // then
        assertThat(room.area).isEqualTo("Test")
    }
}
