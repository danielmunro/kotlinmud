package kotlinmud.persistence.dumper

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomDumperServiceTest {
    @Test
    fun testCanDumpRoom() {
        // setup
        val testService = createTestService(false)

        // given
        val roomDumperService = RoomDumperService(listOf(testService.getStartRoom()))

        // when
        val buffer = roomDumperService.dump()

        // then
        assertThat(buffer).isEqualTo(
            """rooms:
1. start room
tbd~
~

"""
        )
    }
}
