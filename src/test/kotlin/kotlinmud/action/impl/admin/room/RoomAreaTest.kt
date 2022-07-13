package kotlinmud.action.impl.admin.room

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
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
        test.runActionAsAdmin("room area Purgatory")

        // then
        assertThat(room.area.name).isEqualTo("Purgatory")

        // and
        val model = test.getStartRoomModel()
        assertThat(model.area.name).isEqualTo("Purgatory")
    }

    @Test
    fun testCannotSetRoomAreaThatDoesNotExist() {
        // setup
        val test = createTestService()

        // given
        val room = test.getStartRoom()

        // when
        val response = test.runActionAsAdmin("room area Ayooooo")

        // then
        assertThat(response.status).isEqualTo(IOStatus.ERROR)

        // and
        assertThat(room.area.name).isEqualTo("Test")

        // and
        val model = test.getStartRoomModel()
        assertThat(model.area.name).isEqualTo("Test")
    }
}
