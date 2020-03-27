package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.item.InventoryBuilder
import kotlinmud.item.Position
import kotlinmud.test.createTestService
import org.junit.Test

class WearTest {
    @Test
    fun testCanWearEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.buildMob(
            test.mobBuilder()
                .inventory(
                    InventoryBuilder().items(
                        mutableListOf(
                            test.buildItem(
                                test.itemBuilder()
                                    .setPosition(Position.SHIELD)
                                    .setName("a shield")
                            )
                        )
                    ).build()
                )
        )

        // when
        val response = test.runAction(mob, "wear shield")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you wear a shield.")
        assertThat(response.message.toTarget).isEqualTo("$mob wears a shield.")
        assertThat(response.message.toObservers).isEqualTo("$mob wears a shield.")

        // and
        assertThat(mob.inventory.items).hasSize(0)
        assertThat(mob.equipped.items).hasSize(2)
    }

    @Test
    fun testCannotWearItemsThatAreNotEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.buildMob(
            test.mobBuilder()
                .inventory(
                    InventoryBuilder().items(
                        mutableListOf(
                            test.buildItem(
                                test.itemBuilder()
                                    .setName("a book")
                            )
                        )
                    ).build()
                )
        )

        // when
        val response = test.runAction(mob, "wear book")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't equip that.")

        // and
        assertThat(mob.inventory.items).hasSize(1)
        assertThat(mob.equipped.items).hasSize(1)
    }
}
