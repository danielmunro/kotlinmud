package kotlinmud.action.impl.equipment

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.test.helper.createTestService
import org.junit.Test

class WearTest {
    @Test
    fun testCanWearEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val item = test.createItemBuilder().also {
            it.position = Position.SHIELD
            it.type = ItemType.EQUIPMENT
            it.material = Material.IRON
            it.name = "a shield"
        }.build()
        mob.items.add(item)
        val count = mob.equipped.count()

        // when
        val response = test.runAction("wear shield")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you wear a shield.")
        assertThat(response.message.toObservers).isEqualTo("$mob wears a shield.")

        // and
        assertThat(mob.equipped.count()).isEqualTo(count + 1)
    }

    @Test
    fun testCannotWearItemsThatAreNotEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val equippedCount = mob.equipped.count()
        val item = test.createItemBuilder().also {
            it.name = "a book"
            it.material = Material.FLAMMABLE
            it.type = ItemType.FURNITURE
        }.build()
        mob.items.add(item)

        // when
        val response = test.runAction("wear book")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't equip that.")

        // and
        assertThat(mob.equipped.toList()).hasSize(equippedCount)
    }
}
