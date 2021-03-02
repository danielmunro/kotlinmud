package kotlinmud.action.impl.player

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
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
        val mobCard = mob.mobCard!!
        val item = test.createItemBuilder()
                .type(ItemType.FOOD)
                .food(Food.MEAT_PIE)
                .name("a big meat pie")
                .quantity(1)
                .material(Material.ORGANIC)
                .affects(listOf(StunnedAffect().createInstance(timeout)))
                .build()
        transaction {
            mobCard.hunger = 0
            mobCard.thirst = 0
        }
        mob.items.add(item)

        // when
        val response = test.runAction("eat pie")

        // then
        val affect = transaction { mob.affects.find { it.type == AffectType.STUNNED } }!!
        assertThat(affect.type).isEqualTo(AffectType.STUNNED)
        assertThat(affect.timeout).isEqualTo(timeout)

        // and
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }
}
