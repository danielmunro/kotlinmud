package kotlinmud.action.impl.player

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.DrunkAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class DrinkTest {
    @Test
    fun testDrinkImpartsAffects() {
        // setup
        val test = createTestService()

        // given
        val timeout = 2
        val mob = test.createPlayerMob()
        val item = test.createItemBuilder().also {
            it.drink = Drink.BEER
            it.name = "a glass of beer"
            it.quantity = 1
            it.affects = listOf(DrunkAffect().createInstance(timeout))
        }.build()
        mob.items.add(item)

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
        val drink = test.createItemBuilder().also {
            it.name = "a glass of milk"
            it.type = ItemType.DRINK
            it.drink = Drink.MILK
            it.affects = listOf(invis)
        }.build()
        mob.items.add(drink)

        // when
        val response = test.runAction("drink milk")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see anything like that here.")
    }

    @Test
    fun testDrinkDecrementsQuantity() {
        // setup
        val test = createTestService()

        // given
        val quantity = 5
        val mob = test.createPlayerMob()
        val item = test.createItemBuilder().also {
            it.drink = Drink.BEER
            it.name = "a glass of beer"
            it.quantity = quantity
        }.build()
        mob.items.add(item)

        // when
        test.runAction("drink beer")

        // then
        assertThat(item.quantity).isEqualTo(quantity - 1)
    }

    @Test
    fun testCanDrinkFromAFountain() {
        // setup
        val test = createTestService()
        test.createPlayerMob()

        // given
        test.getStartRoom().items.add(
            test.createItemBuilder().also {
                it.name = "a fountain"
                it.type = ItemType.DRINK
                it.drink = Drink.WATER
            }.build()
        )

        // when
        val response = test.runAction("drink fountain")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you drink water from a fountain.")
    }
}
