package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
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
        test.putMobInRoom(mob, test.getStartRoom())
        val mobCard = test.findMobCardByName(mob.name)!!
        val item = test.createItem()
        transaction {
            item.type = ItemType.FOOD
            item.food = Food.MEAT_PIE
            item.name = "a big meat pie"
            item.quantity = 1
            item.mobInventory = mob
            val affect = StunnedAffect().createInstance(timeout)
            affect.item = item
            mobCard.hunger = 0
            mobCard.thirst = 0
        }

        // when
        val response = test.runAction(mob, "eat pie")

        // then
        val affect = transaction { mob.affects.find { it.type == AffectType.STUNNED } }!!
        assertThat(affect.type).isEqualTo(AffectType.STUNNED)
        assertThat(affect.timeout).isEqualTo(timeout)

        // and
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }
}
