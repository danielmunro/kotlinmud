package kotlinmud.action.impl.player

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.DrunkAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.test.helper.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class DrinkTest {
    @Test
    fun testDrinkImpartsAffects() {
        // setup
        val test = createTestService()

        // given
        val timeout = 2
        val mob = test.createPlayerMob()
        mob.room = test.getStartRoom()
        val mobCard = transaction { mob.mobCard!! }
        val item = test.createItemBuilder()
            .drink(Drink.BEER)
            .name("a glass of beer")
            .quantity(1)
            .affects(listOf(DrunkAffect().createInstance(timeout)))
            .material(Material.GLASS)
            .build()
        mob.items.add(item)
        transaction {
            mobCard.thirst = 0
            mobCard.hunger = 0
        }

        // when
        val response = test.runAction("drink beer")

        // then
        val affect = mob.affects.find { it.type == AffectType.DRUNK }!!
        assertThat(affect.type).isEqualTo(AffectType.DRUNK)
        assertThat(affect.timeout).isEqualTo(timeout)

        // and
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testCannotDrinkFromInvisibleDrink() {
        // setup
        val test = createTestService()
        val invis = createAffect(AffectType.INVISIBILITY)
        val mob = test.createPlayerMob()

        // given
        val drink = test.createItemBuilder()
            .name("a glass of milk")
            .type(ItemType.DRINK)
            .drink(Drink.MILK)
            .affects(listOf(invis))
            .material(Material.ORGANIC)
            .build()
        transaction {
            mob.mobCard!!.let {
                it.hunger = 0
                it.thirst = 0
            }
        }
        mob.items.add(drink)

        // when
        val response = test.runAction("drink milk")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see anything like that here.")
    }
}
