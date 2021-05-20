package kotlinmud.action.impl.player

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.test.helper.createTestService
import org.junit.Test

class EatTest {
    @Test
    fun testEatImpartsAffects() {
        // setup
        val test = createTestService()

        // given
        val timeout = 2
        val mob = test.createPlayerMob()
        mob.room = test.getStartRoom()
        val item = test.createItemBuilder().also {
            it.type = ItemType.FOOD
            it.food = Food.MEAT_PIE
            it.name = "a big meat pie"
            it.quantity = 1
            it.material = Material.ORGANIC
            it.affects = listOf(StunnedAffect().createInstance(timeout))
        }.build()
        mob.items.add(item)

        // when
        val response = test.runAction("eat pie")

        // then
        val affect = mob.affects.find { it.type == AffectType.STUNNED }!!
        assertThat(affect.type).isEqualTo(AffectType.STUNNED)
        assertThat(affect.timeout).isEqualTo(timeout)

        // and
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }
}
