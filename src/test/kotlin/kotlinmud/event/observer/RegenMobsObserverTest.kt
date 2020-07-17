package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlinmud.room.type.RegenLevel
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class RegenMobsObserverTest {
    @Test
    fun testRegenRoomDifference() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        val room1 = test.createRoom()
        val room2 = test.createRoom()
        val room3 = test.createRoom()
        val room4 = test.createRoom()
        val room5 = test.createRoom()
        transaction {
            room1.regenLevel = RegenLevel.NONE
            room2.regenLevel = RegenLevel.LOW
            room3.regenLevel = RegenLevel.NORMAL
            room4.regenLevel = RegenLevel.HIGH
            room5.regenLevel = RegenLevel.FULL
        }

        // when
        transaction { mob.hp = 1 }
        test.putMobInRoom(mob, room1)
        test.regenMobs()
        val gain1 = transaction { mob.hp }

        transaction { mob.hp = 1 }
        test.putMobInRoom(mob, room2)
        test.regenMobs()
        val gain2 = transaction { mob.hp }

        transaction { mob.hp = 1 }
        test.putMobInRoom(mob, room3)
        test.regenMobs()
        val gain3 = transaction { mob.hp }

        transaction { mob.hp = 1 }
        test.putMobInRoom(mob, room4)
        test.regenMobs()
        val gain4 = transaction { mob.hp }

        transaction { mob.hp = 1 }
        test.putMobInRoom(mob, room5)
        test.regenMobs()
        val gain5 = transaction { mob.hp }

        // then
        assertThat(gain5).isGreaterThan(gain4)
        assertThat(gain4).isGreaterThan(gain3)
        assertThat(gain3).isGreaterThan(gain2)
        assertThat(gain2).isGreaterThan(gain1)
    }
}
