package kotlinmud.action.impl.admin.room

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomNameTest {
    @Test
    fun testCanChangeRoomName() {
        // setup
        val test = createTestService()

        // given
        val roomName = "a poorly lit alley"

        // when
        val response = test.runAction("room name $roomName")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("room renamed")
        assertThat(test.getStartRoom().name).isEqualTo(roomName)
    }
}
