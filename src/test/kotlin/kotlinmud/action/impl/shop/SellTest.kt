package kotlinmud.action.impl.shop

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class SellTest {
    @Test
    fun testCanSellSanity() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val shopkeeper = testService.createMob {
            it.job = JobType.SHOPKEEPER
        }

        // given
        val item = testService.createItem()
        transaction { item.mobInventory = mob }

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
        transaction { item.mobInventory = mob }

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
        testService.createMob {
            it.job = JobType.SHOPKEEPER
        }

        // given
        val item = testService.createItem()
        transaction { item.mobInventory = mob }

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
        testService.createMob {
            it.job = JobType.SHOPKEEPER
        }

        // given
        val item = testService.createItem {
            it.worth = 100
            it.mobInventory = mob
        }

        // when
        val response = testService.runAction("sell ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("they can't afford that.")
    }
}
