package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
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
        mob.items.plus(itemToPut)
        val itemWithInventory = test.createItem()
        itemWithInventory.room = room

        // when
        val response = test.runAction(mob, "put ${getIdentifyingWord(itemWithInventory)} ${getIdentifyingWord(itemToPut)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you put $itemToPut into $itemWithInventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob puts $itemToPut into $itemWithInventory.")
        assertThat(test.getItemsFor(itemWithInventory)).hasSize(1)
    }

    @Test
    fun testPutItemInItemInventoryAMobIsCarrying() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        val itemToPut = test.createItem()
        mob.items.plus(itemToPut)
        val itemWithInventory = test.createItem()
        mob.items.plus(itemWithInventory)

        // when
        val response = test.runAction(mob, "put ${getIdentifyingWord(itemWithInventory)} ${getIdentifyingWord(itemToPut)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you put $itemToPut into $itemWithInventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob puts $itemToPut into $itemWithInventory.")
        assertThat(test.getItemsFor(itemWithInventory)).hasSize(1)
    }

    @Test
    fun testCannotPutItemInItemThatDoesNotHaveAnInventory() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        val itemToPut = test.createItem()
        mob.items.plus(itemToPut)
        val itemWithInventory = test.createItem()
        mob.items.plus(itemWithInventory)
//        val itemWithInventory = test.buildItem(test.itemBuilder()
//            .hasInventory(false), mob)

        // when
        val response = test.runAction(mob, "put ${getIdentifyingWord(itemWithInventory)} ${getIdentifyingWord(itemToPut)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see anywhere to put that.")
    }
}
