package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlinmud.room.type.RegenLevel
import kotlinmud.test.createTestServiceWithResetDB
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class RegenMobsObserverTest {
    @Test
    fun testRegenRoomDifference() {
        // setup
        val test = createTestServiceWithResetDB()
        val mob = test.createMob()
        val room = test.getStartRoom()

        // when
        val testCase = { regenLevel: RegenLevel ->
            transaction {
                mob.hp = 1
                mob.isNpc = false
                room.regenLevel = regenLevel
                runBlocking { test.regenMobs() }
                mob.hp
            }
        }

        val gain1 = testCase(RegenLevel.NONE)
        val gain2 = testCase(RegenLevel.LOW)
        val gain3 = testCase(RegenLevel.NORMAL)
        val gain4 = testCase(RegenLevel.HIGH)
        val gain5 = testCase(RegenLevel.FULL)

        // then
        assertThat(gain5).isGreaterThan(gain4)
        assertThat(gain4).isGreaterThan(gain3)
        assertThat(gain3).isGreaterThan(gain2)
        assertThat(gain2).isGreaterThan(gain1)
    }
}
