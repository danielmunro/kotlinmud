package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlinmud.event.factory.createFightRoundEvent
import kotlinmud.room.repository.findRoomByMobId
import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class WimpyObserverTest {
    @Test
    fun testWimpyInvokesFleeMovesMob() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val dst = test.createRoom()
        transaction { test.getStartRoom().east = dst }
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
