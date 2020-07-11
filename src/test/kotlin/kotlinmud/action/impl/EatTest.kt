package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.test.createTestService
import org.junit.Test

class EatTest {
    @Test
    fun testEatImpartsAffects() {
        // setup
        val test = createTestService()

        // given
        val timeout = 2
        val mob = test.createPlayerMob()
        test.putMobInRoom(mob, test.getStartRoom())
        val mobCard = test.findMobCardByName(mob.name)!!
        val item = test.createItem()
        item.type = ItemType.FOOD
        item.food = Food.MEAT_PIE
        item.name = "a big meat pie"
        item.affects.plus(StunnedAffect().createInstance(timeout))
        mobCard.appetite.decrement()

        // when
        val response = test.runAction(mob, "eat pie")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
        assertThat(mob.affects.count()).isEqualTo(1)
        val affect = mob.affects.find { it.type == AffectType.STUNNED }!!
        assertThat(affect.type).isEqualTo(AffectType.STUNNED)
        assertThat(affect.timeout).isEqualTo(timeout)
    }
}
