package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlinmud.room.type.RegenLevel
import kotlinmud.test.createTestService
import org.junit.Test

class RegenMobsObserverTest {
    @Test
    fun testRegenRoomDifference() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val rooms = test.getRooms()

        // given
        val room1 = rooms.find { it.regenLevel == RegenLevel.NONE }!!
        val room2 = rooms.find { it.regenLevel == RegenLevel.LOW }!!
        val room3 = rooms.find { it.regenLevel == RegenLevel.NORMAL }!!
        val room4 = rooms.find { it.regenLevel == RegenLevel.HIGH }!!
        val room5 = rooms.find { it.regenLevel == RegenLevel.FULL }!!

        // when
        mob.hp = 1
        test.putMobInRoom(mob, room1)
        test.regenMobs()
        val gain1 = mob.hp

        mob.hp = 1
        test.putMobInRoom(mob, room2)
        test.regenMobs()
        val gain2 = mob.hp

        mob.hp = 1
        test.putMobInRoom(mob, room3)
        test.regenMobs()
        val gain3 = mob.hp

        mob.hp = 1
        test.putMobInRoom(mob, room4)
        test.regenMobs()
        val gain4 = mob.hp

        mob.hp = 1
        test.putMobInRoom(mob, room5)
        test.regenMobs()
        val gain5 = mob.hp

        // then
        assertThat(gain5).isGreaterThan(gain4)
        assertThat(gain4).isGreaterThan(gain3)
        assertThat(gain3).isGreaterThan(gain2)
        assertThat(gain2).isGreaterThan(gain1)
    }
}
