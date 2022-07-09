package kotlinmud.persistence.dumper

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.room.factory.createTestArea
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomDumperServiceTest {
    @Test
    fun testCanDumpRoom() {
        // setup
        val testService = createTestService(false)

        // given
        val roomDumperService = RoomDumperService(createTestArea(), listOf(testService.getStartRoom()))

        // when
        val buffer = roomDumperService.dump()

        // then
        assertThat(buffer).isEqualTo(
            """rooms:
1. start room
tbd~
north 0~
south 0~
east 0~
west 0~
up 0~
down 0~
~

"""
        )
    }
}
