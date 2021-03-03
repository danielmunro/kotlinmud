package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
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
        val item = testService.createItem()
        room.items.add(item)
        val roomItemCount = testService.countItemsFor(room)
        val mobItemCount = testService.countItemsFor(mob)

        // when
        val response = testService.runAction("get ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you pick up $item.")
        assertThat(testService.countItemsFor(mob)).isEqualTo(mobItemCount + 1)
        assertThat(room.items).hasSize(roomItemCount - 1)
    }

    @Test
    fun testMobCannotGetNonexistentItemFromRoom() {
        // setup
        val testService = createTestServiceWithResetDB()
        val mob = testService.createMob()
        val room = mob.room
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
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMobBuilder()
            .maxItems(0)
            .build()
        val item = test.createItem()
        test.getStartRoom().items.add(item)

        // when
        val response = test.runAction(mob, "get ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you cannot carry any more.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testMobCannotGetMoreWeightThanTheyCanHold() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMobBuilder()
            .maxWeight(0)
            .build()
        val item = test.createItemBuilder()
            .weight(1.0)
            .type(ItemType.FOOD)
            .material(Material.ORGANIC)
            .build()
        test.getStartRoom().items.add(item)

        // when
        val response = test.runAction(mob, "get ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you cannot carry any more.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testMobCannotGetMoreItemsFromAContainerThanTheyCanHold() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMobBuilder().maxItems(1).build()
        val container = test.createContainer()
        mob.items.add(container)
        val item = test.createItem()
        container.items!!.add(item)

        // when
        val response = test.runAction(mob, "get ${getIdentifyingWord(item)} ${getIdentifyingWord(container)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you cannot carry any more.")
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testMobCannotPutTooManyItemsIntoAContainer() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMob()
        val container = test.createItemBuilder()
            .type(ItemType.CONTAINER)
            .maxItems(0)
            .build()
        mob.items.add(container)
        val item = test.createItem()
        mob.items.add(item)

        // when
        val response = test.runAction("put ${getIdentifyingWord(item)} ${getIdentifyingWord(container)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("that is too heavy.")
    }

    @Test
    fun testMobCannotPutTooMuchWeightIntoAContainer() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMob()
        val container = test.createItemBuilder()
            .maxWeight(0)
            .maxItems(100)
            .type(ItemType.CONTAINER)
            .build()
        mob.items.add(container)
        val item = test.createItemBuilder()
            .weight(1.0)
            .build()
        mob.items.add(item)

        // when
        val response = test.runAction("put ${getIdentifyingWord(item)} ${getIdentifyingWord(container)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("that is too heavy.")
    }
}
