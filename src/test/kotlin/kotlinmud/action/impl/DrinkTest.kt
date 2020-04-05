package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.affect.AffectType
import kotlinmud.affect.impl.DrunkAffect
import kotlinmud.io.IOStatus
import kotlinmud.item.Drink
import kotlinmud.test.createTestService
import org.junit.Test

class DrinkTest {
    @Test
    fun testDrinkImpartsAffects() {
        // setup
        val test = createTestService()

        // given
        val timeout = 2
        val mob = test.createMob()
        test.buildItem(test.itemBuilder()
            .drink(Drink.BEER)
            .name("a glass of beer")
            .quantity(1)
            .affects(mutableListOf(DrunkAffect().createInstance(timeout))), mob)
        mob.appetite.decrement()

        // when
        val response = test.runAction(mob, "drink beer")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
        assertThat(mob.affects().getAffects()).hasSize(1)
        val affect = mob.affects().findByType(AffectType.DRUNK)!!
        assertThat(affect.affectType).isEqualTo(AffectType.DRUNK)
        assertThat(affect.timeout).isEqualTo(timeout)
    }
}
