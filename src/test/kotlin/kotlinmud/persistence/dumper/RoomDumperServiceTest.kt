package kotlinmud.persistence.dumper

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.room.factory.createPurgatoryArea
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RoomDumperServiceTest {
    @Test
    fun testCanDumpRoom() {
        // setup
        val testService = createTestService(false)
        val startupService = testService.createStartupService(
            listOf(
                """
area:
1. Purgatory
~

rooms:
1. a test room
a test room~
n 2~
~

2. another test room
a test room~
s 1~
~

"""
            )
        )
        startupService.hydrateWorld()

        // given
        val roomDumperService = testService.getRoomDumperService(createPurgatoryArea())

        // when
        val buffer = roomDumperService.dump()

        // then
        assertThat(buffer).isEqualTo(
            """rooms:
1. a test room
a test room~
n 2~
~

2. another test room
a test room~
s 1~
~

"""
        )
    }
}
