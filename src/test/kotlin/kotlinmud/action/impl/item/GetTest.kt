package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class GetTest {
    @Test
    fun testMobCanGetItemFromRoom() {
        // setup
        val testService = createTestServiceWithResetDB()
        val mob = testService.createMob()
        val room = transaction { mob.room }
        val item = testService.createItem { it.room = room }
        val roomItemCount = testService.countItemsFor(room)
        val mobItemCount = testService.countItemsFor(mob)

        // when
        val response = testService.runAction("get ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you pick up $item.")
        assertThat(testService.countItemsFor(mob)).isEqualTo(mobItemCount + 1)
        assertThat(testService.countItemsFor(room)).isEqualTo(roomItemCount - 1)
    }

    @Test
    fun testMobCannotGetNonexistentItemFromRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = transaction { mob.room }
        val itemCount = testService.countItemsFor(room)

        // when
        val response = testService.runAction("get foo")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see that anywhere.")
        assertThat(testService.countItemsFor(mob)).isEqualTo(1)
        assertThat(testService.countItemsFor(room)).isEqualTo(itemCount)
    }

    @Test
    fun testMobCannotGetMoreItemsFromARoomThanTheyCanHold() {
        // setup
        val test = createTestService()

        // given
        test.createMob { it.maxItems = 1 }
        val item = test.createItem { it.room = test.getStartRoom() }

        // when
        val response = test.runAction("get ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you cannot carry any more.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testMobCannotGetMoreWeightThanTheyCanHold() {
        // setup
        val test = createTestService()

        // given
        test.createMob { it.maxWeight = 0 }
        val item = test.createItem {
            it.room = test.getStartRoom()
            it.weight = 1.0
        }

        // when
        val response = test.runAction("get ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("that is too heavy.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testMobCannotGetMoreItemsFromAContainerThanTheyCanHold() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob { it.maxItems = 1 }
        val container = test.createContainer { it.mobInventory = mob }
        val item = test.createItem { it.container = container }

        // when
        val response = test.runAction("get ${getIdentifyingWord(item)} ${getIdentifyingWord(container)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you cannot carry any more.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testMobCannotPutTooManyItemsIntoAContainer() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val container = test.createContainer {
            it.mobInventory = mob
            it.maxItems = 0
        }
        val item = test.createItem {
            it.mobInventory = mob
        }

        // when
        val response = test.runAction("put ${getIdentifyingWord(item)} ${getIdentifyingWord(container)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("that is full.")
    }

    @Test
    fun testMobCannotPutTooMuchWeightIntoAContainer() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val container = test.createContainer {
            it.mobInventory = mob
            it.maxWeight = 0
        }
        val item = test.createItem {
            it.mobInventory = mob
            it.weight = 1.0
        }

        // when
        val response = test.runAction("put ${getIdentifyingWord(item)} ${getIdentifyingWord(container)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("that is too heavy.")
    }
}
