package kotlinmud.item.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class ItemServiceTest {
    @Test
    fun testItemCanDecay() {
        // setup
        val test = createTestService()

        // given
        val amount = 5
        val item = test.createItemBuilder()
                .decayTimer(amount)
                .build()

        // when
        test.callDecayEvent()

        // then
        assertThat(item.decayTimer).isEqualTo(amount - 1)
    }

    @Test
    fun testItemIsRemovedFromMobInventoryWhenDecayed() {
        // setup
        val test = createTestService()

        // given
        val amount = 1
        val item = test.createItemBuilder()
                .decayTimer(amount)
                .build()
        val mob = test.createMob()
        mob.items.add(item)

        // when
        test.callDecayEvent()

        // then
        assertThat(item.decayTimer).isEqualTo(amount - 1)
        assertThat(mob.items).hasSize(0)
    }

    @Test
    fun testItemIsRemovedFromRoomInventoryWhenDecayed() {
        // setup
        val test = createTestService()

        // given
        val amount = 1
        val item = test.createItemBuilder()
                .decayTimer(amount)
                .build()
        val room = test.getStartRoom()
        room.items.add(item)

        // when
        test.callDecayEvent()

        // then
        assertThat(item.decayTimer).isEqualTo(amount - 1)
        assertThat(room.items).hasSize(0)
    }
}