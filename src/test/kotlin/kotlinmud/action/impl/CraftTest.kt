package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.item.ItemType
import kotlinmud.test.createTestService
import org.junit.Test

class CraftTest {
    @Test
    fun testCanCraftBuildersTable() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        test.buildItem(test.itemBuilder().type(ItemType.LUMBER), mob)
        test.buildItem(test.itemBuilder().type(ItemType.LUMBER), mob)
        test.buildItem(test.itemBuilder().type(ItemType.LUMBER), mob)
        val itemCount = test.getItemsFor(mob).size

        // when
        val response = test.runAction(mob, "craft builder")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you craft a builder's table.")
        assertThat(test.getItemsFor(mob)).hasSize(itemCount - 2)
    }

    @Test
    fun testCraftingNeedsAllRequiredItems() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // when
        val response = test.runAction(mob, "craft builder")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't have all the necessary components.")
        assertThat(test.getItemsFor(mob)).hasSize(1)
    }
}
