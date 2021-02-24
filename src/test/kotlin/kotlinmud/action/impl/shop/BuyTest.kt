package kotlinmud.action.impl.shop

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class BuyTest {
    @Test
    fun testBuySanityCheck() {
        // setup
        val test = createTestService()
        test.createMob()
        val shop = test.createShopkeeper()

        // given
        val item = test.createItem()
        shop.items.add(item)

        // when
        val response = test.runAction("buy ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you buy $item from $shop for 0 gold.")
    }

    @Test
    fun testBuyNeedsMoney() {
        // setup
        val test = createTestService()
        test.createMob()
        val shop = test.createShopkeeper()

        // given
        val item = test.createItem {
            it.worth = 10
        }
        shop.items.add(item)

        // when
        val response = test.runAction("buy ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't afford that.")
    }

    @Test
    fun testBuyNeedsShopkeeper() {
        // setup
        val test = createTestService()
        test.createMob()

        // given
        val mob = test.createMob()
        val item = test.createItem()
        mob.items.add(item)

        // when
        val response = test.runAction("buy ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("no merchant is here.")
    }

    @Test
    fun testBuyNeedsItem() {
        // setup
        val test = createTestService()
        test.createMob()

        // given
        test.createShopkeeper()

        // when
        val response = test.runAction("buy foo")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("they don't have anything like that.")
    }
}
