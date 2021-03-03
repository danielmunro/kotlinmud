package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.contains
import kotlinmud.test.helper.createTestService
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
        mob.items.addAll(listOf(item1, item2))

        // when
        val response = testService.runAction("inv")

        // then
        assertThat(response.message.toActionCreator).contains(item1.name)
        assertThat(response.message.toActionCreator).contains(item2.name)
    }
}
