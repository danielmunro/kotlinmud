package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class DropTest {
    @Test
    fun testMobCanDropItem() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val item = testService.createItem()

        // given
        mob.items.add(item)
        val room = mob.room
        val mobItemCount = mob.items.size
        val roomItemCount = room.items.size

        // when
        val response = testService.runAction("drop ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you drop $item.")
        assertThat(mob.items.size).isEqualTo(mobItemCount - 1)
        assertThat(room.items).hasSize(roomItemCount + 1)
    }
}
