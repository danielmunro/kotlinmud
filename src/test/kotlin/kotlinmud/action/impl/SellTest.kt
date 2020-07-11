package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class SellTest {
    @Test
    fun testCanSellSanity() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val shopkeeper = testService.createMob()
        shopkeeper.job = JobType.SHOPKEEPER

        // given
        val item = testService.createItem()
        item.mobInventory = mob

        // when
        val response = testService.runAction(mob, "sell ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator)
            .isEqualTo("you sell $item to $shopkeeper for ${item.worth} gold.")
    }
}
