package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.contains
import kotlinmud.test.createTestService
import org.junit.Test

class InventoryTest {
    @Test
    fun testInventoryIsDisplayed() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        val item1 = testService.createItem(mob.inventory)
        val item2 = testService.createItem(mob.inventory)

        // when
        val response = testService.runAction(mob, "inv")

        // then
        assertThat(response.message.toActionCreator).contains(item1.name)
        assertThat(response.message.toActionCreator).contains(item2.name)
    }
}
