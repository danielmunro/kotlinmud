package kotlinmud.action.impl.shop

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class BuyTest {
    @Test
    fun testBuySanityCheck() {
        // setup
        val test = createTestService()
        test.createMob()
        val shop = test.createMob {
            it.job = JobType.SHOPKEEPER
        }
        val item = test.createItem {
            it.mobInventory = shop
        }

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
        val shop = test.createMob {
            it.job = JobType.SHOPKEEPER
        }

        // given
        val item = test.createItem {
            it.mobInventory = shop
            it.worth = 10
        }

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
        val item = test.createItem {
            it.mobInventory = mob
        }

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
        test.createMob {
            it.job = JobType.SHOPKEEPER
        }

        // when
        val response = test.runAction("buy foo")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("they don't have anything like that.")
    }
}
