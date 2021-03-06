package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class PutTest {
    @Test
    fun testPutItemInItemInventoryInAGivenRoom() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val room = test.getStartRoom()

        // given
        val itemToPut = test.createItem()
        mob.items.add(itemToPut)
        val itemWithInventory = test.createContainer()
        room.items.add(itemWithInventory)

        // when
        val response = test.runAction("put ${getIdentifyingWord(itemToPut)} ${getIdentifyingWord(itemWithInventory)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you put $itemToPut into $itemWithInventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob puts $itemToPut into $itemWithInventory.")
        assertThat(itemWithInventory.items!!).hasSize(1)
    }

    @Test
    fun testPutItemInItemInventoryAMobIsCarrying() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        val itemToPut = test.createItem()
        val itemWithInventory = test.createContainer()
        mob.items.addAll(listOf(itemToPut, itemWithInventory))

        // when
        val response = test.runAction("put ${getIdentifyingWord(itemToPut)} ${getIdentifyingWord(itemWithInventory)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you put $itemToPut into $itemWithInventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob puts $itemToPut into $itemWithInventory.")
        assertThat(itemWithInventory.items!!).hasSize(1)
    }

    @Test
    fun testCannotPutItemInItemThatDoesNotHaveAnInventory() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        val itemToPut = test.createItem()
        val itemWithNoInventory = test.createItem()
        mob.items.addAll(listOf(itemToPut, itemWithNoInventory))

        // when
        val response = test.runAction("put ${getIdentifyingWord(itemToPut)} ${getIdentifyingWord(itemWithNoInventory)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see that anywhere.")
    }
}
