package kotlinmud.action.impl

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
        test.putMobInRoom(mob, test.getStartRoom())
        val mobCard = test.findMobCardByName(mob.name)!!
        val item = test.createItem()
        transaction {
            item.drink = Drink.BEER
            item.name = "a glass of beer"
            item.quantity = 1
            item.mobInventory = mob
            val affect = DrunkAffect().createInstance(timeout)
            affect.item = item
        }
        mobCard.appetite.decrement()

        // when
        val response = test.runAction(mob, "drink beer")

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
        val mobCard = test.getMobCardForMob(mob)!!

        // given
        mobCard.appetite.decrement()
        transaction {
            drink.name = "a glass of milk"
            drink.mobInventory = mob
            drink.type = ItemType.DRINK
            drink.drink = Drink.MILK
            invis.item = drink
        }

        // when
        val response = test.runAction(mob, "drink milk")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see anything like that here.")
    }
}
