package kotlinmud.action.impl.equipment

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.item.type.Position
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class WearTest {
    @Test
    fun testCanWearEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val item = test.createItem()
        transaction {
            item.position = Position.SHIELD
            item.name = "a shield"
            item.mobInventory = mob
        }
        val count = transaction { mob.equipped.count() }

        // when
        val response = test.runAction(mob, "wear shield")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you wear a shield.")
        assertThat(response.message.toObservers).isEqualTo("$mob wears a shield.")

        // and
        assertThat(transaction { mob.equipped.count() }).isEqualTo(count + 1)
    }

    @Test
    fun testCannotWearItemsThatAreNotEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val equippedCount = transaction { mob.equipped.count() }
        val item = test.createItem()
        transaction {
            item.name = "a book"
            item.mobInventory = mob
        }

        // when
        val response = test.runAction(mob, "wear book")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't equip that.")

        // and
        assertThat(transaction { mob.equipped.toList() }).hasSize(equippedCount)
    }
}
