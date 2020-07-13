package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class GetTest {
    @Test
    fun testMobCanGetItemFromRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)
        val item = testService.createItem()
        transaction { item.room = room }
        val roomItemCount = testService.countItemsFor(room)
        val mobItemCount = testService.countItemsFor(mob)

        // when
        val response = testService.runAction(mob, "get ${getIdentifyingWord(item)}")

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
        val room = testService.getRoomForMob(mob)
        val itemCount = testService.countItemsFor(room)

        // when
        val response = testService.runAction(mob, "get foo")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see that anywhere.")
        assertThat(testService.countItemsFor(mob)).isEqualTo(1)
        assertThat(testService.countItemsFor(room)).isEqualTo(itemCount)
    }
}
