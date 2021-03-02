package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class DropTest {
    @Test
    fun testMobCanDropItem() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val item = testService.createItem()
        mob.items.add(item)
        val room = transaction { mob.room }
        val mobItemCount = testService.countItemsFor(mob)
        val roomItemCount = testService.countItemsFor(room)

        // when
        val response = testService.runAction("drop ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you drop $item.")
        assertThat(testService.countItemsFor(mob)).isEqualTo(mobItemCount - 1)
        assertThat(transaction { room.items.count() }).isEqualTo(roomItemCount + 1)
    }
}
