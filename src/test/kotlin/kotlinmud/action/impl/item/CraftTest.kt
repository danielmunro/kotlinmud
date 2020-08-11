package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import org.junit.Test

class CraftTest {
    @Test
    fun testCanCraftBuildersTable() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        test.make(3)
            .lumber()
            .andGiveTo(mob)
        val itemCount = test.findAllItemsByOwner(mob).size

        // when
        val response = test.runAction(mob, "craft builder")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you craft a builder's table.")
        assertThat(test.findAllItemsByOwner(mob)).hasSize(itemCount - 2)
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
        assertThat(test.findAllItemsByOwner(mob)).hasSize(1)
    }
}
