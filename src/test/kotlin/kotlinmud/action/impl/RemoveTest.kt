package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.item.InventoryBuilder
import kotlinmud.item.Position
import kotlinmud.test.createTestService
import org.junit.Test

class RemoveTest {
    @Test
    fun testCanRemoveEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.buildMob(
            test.mobBuilder().equipped(
                InventoryBuilder().items(
                    mutableListOf(test.buildItem(
                        test.itemBuilder()
                            .position(Position.SHIELD)
                            .name("a shield")
                    ))
                ).build()
            )
        )

        // when
        val response = test.runAction(mob, "remove shield")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you remove a shield and put it in your inventory.")
        assertThat(response.message.toTarget).isEqualTo("$mob removes a shield and puts it in their inventory.")
        assertThat(response.message.toObservers).isEqualTo("$mob removes a shield and puts it in their inventory.")

        // and
        assertThat(mob.equipped.items).hasSize(1)
        assertThat(mob.inventory.items).hasSize(1)
    }
}
