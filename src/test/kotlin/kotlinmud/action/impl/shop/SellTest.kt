package kotlinmud.action.impl.shop

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class SellTest {
    @Test
    fun testCanSellSanity() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val shopkeeper = testService.createShopkeeper()

        // given
        val item = testService.createItem()
        mob.items.add(item)

        // when
        val response = testService.runAction("sell ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator)
            .isEqualTo("you sell $item to $shopkeeper for ${item.worth} gold.")
    }

    @Test
    fun testCannotSellIfShopkeeperIsNotInRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        testService.createMob()

        // given
        val item = testService.createItem()
        mob.items.add(item)

        // when
        val response = testService.runAction("sell ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("no merchant is here.")
    }

    @Test
    fun testCannotSellItemThatIsNotInInventory() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        testService.createShopkeeper()

        // given
        val item = testService.createItem()
        mob.items.add(item)

        // when
        val response = testService.runAction("sell foobar")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't have anything like that.")
    }

    @Test
    fun testCannotSellItemThatMerchantCannotAfford() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        testService.createShopkeeper()

        // given
        val item = testService.createItemBuilder()
            .worth(100)
            .build()
        mob.items.add(item)

        // when
        val response = testService.runAction("sell ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("they can't afford that.")
    }
}
