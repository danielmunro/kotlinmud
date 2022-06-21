package kotlinmud.action.impl.admin.room

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomDescriptionChangeTest {
    @Test
    fun testChangeRoomDescription() {
        // setup
        val test = createTestService()

        // given
        val room = test.getStartRoom()
        val description = """A long and narrow trail meanders through a lush forest. Besides the occasional
call from a nearby bird, eerie silence prevails."""
        val change = "call from a nearby bird, the wind whistles through the trees."
        room.description = description

        // when
        test.runActionAsAdmin("room description change 1 $change")

        // then
        assertThat(room.description).isEqualTo(
            """A long and narrow trail meanders through a lush forest. Besides the occasional
call from a nearby bird, the wind whistles through the trees."""
        )
    }
}
