package kotlinmud.action.impl.admin.room

import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomCloneTest {
    @Test
    fun testCanCloneRoom() {
        // setup
        val test = createTestService()

        // given
        val room = test.getStartRoom()

        // expect
        assertThat(room.north).isNull()

        // when
        test.runActionAsAdmin("room clone north")

        // then
        assertThat(room.north).isNotNull()
    }
}
