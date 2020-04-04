package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.affect.AffectType
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.io.IOStatus
import kotlinmud.item.Food
import kotlinmud.item.InventoryBuilder
import kotlinmud.test.createTestService
import org.junit.Test

class EatTest {
    @Test
    fun testEatImpartsAffects() {
        // setup
        val test = createTestService()

        // given
        val timeout = 2
        val mob = test.withMob {
            it.inventory(
                InventoryBuilder().items(
                    mutableListOf(
                        test.buildItem(
                            test.itemBuilder()
                                .food(Food.MEAT_PIE)
                                .name("a big meat pie")
                                .affects(mutableListOf(StunnedAffect().createInstance(timeout)))
                        )
                    )
                ).build()
            )
        }
        mob.appetite.decrement()

        // when
        val response = test.runAction(mob, "eat pie")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
        assertThat(mob.affects().getAffects()).hasSize(1)
        val affect = mob.affects().findByType(AffectType.STUNNED)!!
        assertThat(affect.affectType).isEqualTo(AffectType.STUNNED)
        assertThat(affect.timeout).isEqualTo(timeout)
    }
}
