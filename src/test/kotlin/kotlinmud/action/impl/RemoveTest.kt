package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.item.type.Position
import kotlinmud.test.createTestService
import org.junit.Test

class RemoveTest {
    @Test
    fun testCanRemoveEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val item = test.createItem()
        item.position = Position.SHIELD
        item.name = "a shield"
        mob.equipped.plus(item)
        val equippedAmount = mob.equipped.count()

        // when
        val response = test.runAction(mob, "remove shield")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you remove a shield and put it in your inventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob removes a shield and puts it in their inventory.")

        // and
        assertThat(mob.equipped.count()).isEqualTo(equippedAmount - 1)
    }
}
