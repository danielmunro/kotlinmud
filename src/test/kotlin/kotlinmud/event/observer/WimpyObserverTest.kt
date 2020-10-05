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
    fun testWimpySanity() {
        // setup
        val test = createTestService()

        // given
        transaction { test.getStartRoom().east = test.createRoom() }

        // expect
        test.createMob {
            it.wimpy = 0
        }.let {
            assertThat(transaction { it.isWimpyMode() }).isFalse()
        }

        // expect
        test.createMob {
            it.wimpy = 5
            it.hp = 1
        }.let {
            assertThat(transaction { it.isWimpyMode() }).isTrue()
        }

        // expect
        test.createMob {
            it.wimpy = 0
            it.hp = 0
        }.let {
            assertThat(transaction { it.isWimpyMode() }).isFalse()
        }
    }

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
        assertThat(findRoomByMobId(mob.id.value)).isEqualTo(dst)
    }
}
