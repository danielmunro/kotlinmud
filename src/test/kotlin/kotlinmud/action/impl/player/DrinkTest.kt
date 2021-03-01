package kotlinmud.action.impl.player

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.DrunkAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemType
import kotlinmud.test.createTestService
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
        val item = test.createItem()
        transaction {
            item.drink = Drink.BEER
            item.name = "a glass of beer"
            item.quantity = 1
            item.affects.plus(DrunkAffect().createInstance(timeout))
            mobCard.thirst = 0
            mobCard.hunger = 0
        }
        mob.items.add(item)

        // when
        val response = test.runAction("drink beer")

        // then
        val affect = transaction { mob.affects.find { it.type == AffectType.DRUNK }!! }
        assertThat(affect.type).isEqualTo(AffectType.DRUNK)
        assertThat(affect.timeout).isEqualTo(timeout)

        // and
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testCannotDrinkFromInvisibleDrink() {
        // setup
        val test = createTestService()
        val drink = test.createItem()
        val invis = createAffect(AffectType.INVISIBILITY)
        val mob = test.createPlayerMob()

        // given
        transaction {
            drink.name = "a glass of milk"
            drink.type = ItemType.DRINK
            drink.drink = Drink.MILK
            drink.affects.plus(invis)
            mob.mobCard?.let {
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
