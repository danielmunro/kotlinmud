package kotlinmud.action.impl.equipment

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.item.type.Position
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RemoveTest {
    @Test
    fun testCanRemoveEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val item = test.createItemBuilder()
            .position(Position.SHIELD)
            .name("a shield")
            .build()
        mob.equipped.add(item)
        val equippedAmount = mob.equipped.count()

        // when
        val response = test.runAction("remove shield")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you remove a shield and put it in your inventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob removes a shield and puts it in their inventory.")

        // and
        assertThat(mob.equipped).hasSize(equippedAmount - 1)
    }
}
