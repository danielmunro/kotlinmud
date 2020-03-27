package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.JobType
import kotlinmud.test.createTestService
import org.junit.Test

class SellTest {
    @Test
    fun testCanSellSanity() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val shopkeeper = testService.createMob(JobType.SHOPKEEPER)

        // given
        val item = testService.createItem(mob.inventory)

        // when
        val response = testService.runAction(mob, "sell the")

        // then
        assertThat(response.message.toActionCreator)
            .isEqualTo("you sell $item to $shopkeeper for ${item.worth} gold.")
    }
}
