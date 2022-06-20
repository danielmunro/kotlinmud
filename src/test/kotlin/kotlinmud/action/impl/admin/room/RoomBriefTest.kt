package kotlinmud.action.impl.admin.room

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomBriefTest {
    @Test
    fun testCanChangeRoomBrief() {
        // setup
        val test = createTestService()

        // given
        val room = test.getStartRoom()
        val brief = "a decaying dock going out to a pond"

        // when
        test.runActionAsAdmin("room brief $brief")

        // then
        assertThat(room.brief).isEqualTo(brief)
    }
}
