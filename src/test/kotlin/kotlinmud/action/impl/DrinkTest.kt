package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.impl.DrunkAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Drink
import kotlinmud.test.createTestService
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
        item.drink = Drink.BEER
        item.name = "a glass of beer"
        item.quantity = 1
        item.affects.plus(DrunkAffect().createInstance(timeout))
        mobCard.appetite.decrement()

        // when
        val response = test.runAction(mob, "drink beer")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
        assertThat(mob.affects.count()).isEqualTo(1)
        val affect = mob.affects.find { it.type == AffectType.DRUNK }!!
        assertThat(affect.type).isEqualTo(AffectType.DRUNK)
        assertThat(affect.timeout).isEqualTo(timeout)
    }
}
