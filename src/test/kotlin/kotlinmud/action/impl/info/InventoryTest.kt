package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.contains
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class InventoryTest {
    @Test
    fun testInventoryIsDisplayed() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        val item1 = testService.createItem()
        val item2 = testService.createItem()
        transaction {
            item1.mobInventory = mob
            item2.mobInventory = mob
        }

        // when
        val response = testService.runAction(mob, "inv")

        // then
        assertThat(response.message.toActionCreator).contains(item1.name)
        assertThat(response.message.toActionCreator).contains(item2.name)
    }
}
