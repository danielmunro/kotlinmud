package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class GetFromItemTest {
    @Test
    fun testCanGetFromItemInRoomInventory() {
        // setup
        val test = createTestServiceWithResetDB()
        val mob = test.createMob()

        // given
        val itemWithInventory = test.createContainer {
            it.isContainer = true
        }
        mob.items.add(itemWithInventory)
        val item = test.createItem { it.container = itemWithInventory }

        // when
        val response = test.runAction("get ${getIdentifyingWord(item)} ${getIdentifyingWord(itemWithInventory)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you get $item from $itemWithInventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob gets $item from $itemWithInventory.")
    }

    @Test
    fun testCanGetFromItemInMobInventory() {
        // setup
        val test = createTestServiceWithResetDB()
        val mob = test.createMob()

        // given
        val itemWithInventory = test.createContainer {
            it.isContainer = true
        }
        mob.items.add(itemWithInventory)
        val item = test.createItem { it.container = itemWithInventory }

        // when
        val response = test.runAction("get ${getIdentifyingWord(item)} ${getIdentifyingWord(itemWithInventory)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you get $item from $itemWithInventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob gets $item from $itemWithInventory.")
    }
}
