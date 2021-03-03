package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.event.factory.createFightRoundEvent
import kotlinmud.test.helper.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class WimpyObserverTest {
    @Test
    fun testWimpyInvokesFleeMovesMob() {
        // setup
        val test = createTestService()

        // given
        val dst = test.createRoom()
        test.getStartRoom().east = dst
        val mob = test.createMob {
            it.wimpy = 10
            it.hp = 0
        }
        test.addFight(mob, test.createMob())

        // expect
        assertThat(transaction { mob.room }).isEqualTo(test.getStartRoom())

        // when
        val fight = test.addFight(mob, mob)
        transaction {
            test.callWimpyEvent(fight.createFightRoundEvent())
        }

        // then
        assertThat(mob.room).isEqualTo(dst)
    }
}
