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
        val mob = test.buildMob(
            test.mobBuilder().addItem(
                test.buildItem(
                    test.itemBuilder()
                        .setDrink(Drink.BEER)
                        .setName("a glass of beer")
                        .addAffect(DrunkAffect().createInstance(timeout))
                )
            )
        )
        mob.appetite.decrement()

        // when
        val response = test.runAction(mob, "drink beer")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
        assertThat(mob.affects).hasSize(1)
        val affect = mob.affects.first()
        assertThat(affect.affectType).isEqualTo(AffectType.DRUNK)
        assertThat(affect.timeout).isEqualTo(timeout)
    }
}
