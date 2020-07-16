package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class GetFromItemTest {
    @Test
    fun testCanGetFromItemInRoomInventory() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val room = test.getStartRoom()

        // given
        val itemWithInventory = test.createItem()
        val item = test.createItem()
        transaction {
            itemWithInventory.isContainer = true
            item.container = itemWithInventory
        }

        // when
        val response = test.runAction(mob, "get ${getIdentifyingWord(itemWithInventory)} ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you get $item from $itemWithInventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob gets $item from $itemWithInventory.")
    }

    @Test
    fun testCanGetFromItemInMobInventory() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        val itemWithInventory = test.createItem()
        val item = test.createItem()
        transaction {
            itemWithInventory.isContainer = true
            item.container = itemWithInventory
        }

        // when
        val response = test.runAction(mob, "get ${getIdentifyingWord(itemWithInventory)} ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you get $item from $itemWithInventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob gets $item from $itemWithInventory.")
    }
}
