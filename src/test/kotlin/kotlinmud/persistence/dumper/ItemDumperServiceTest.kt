package kotlinmud.persistence.dumper

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.room.factory.createPurgatoryArea
import kotlinmud.test.helper.createTestService
import org.junit.Test

class ItemDumperServiceTest {
    @Test
    fun testItemsCanBeDumped() {
        // setup
        val test = createTestService(false)

        // given
        val startupService = test.createStartupService(
            listOf(
                """
area:
1. Purgatory
~

rooms:
1. a test room
a test room~
~

items:
1. a flower
a flower has been left on the ground~
a description~
type other~
material organic~
~
room 10 100 1~
~
"""
            )
        )
        startupService.hydrateWorld()
        val itemDumperService = test.getItemDumperService(createPurgatoryArea())

        // when
        val dump = itemDumperService.dump()

        // then
        assertThat(dump).isEqualTo(
            """items:
1. a flower
a flower has been left on the ground~
a description~
type other~
material organic~
~
room 10 100 1~
~

"""
        )
    }
}
