package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class MobControllerTest {
    @Test
    fun testCanWalkOnDefinedPath() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val controller = test.createMobController(mob)
        val room1 = test.getStartRoom()
        val room2 = test.createRoom()
        val room3 = test.createRoom()
        transaction {
            room1.east = room2
            room2.west = room1
            room2.south = room3
            room3.north = room2
            mob.job = JobType.PATROL
            mob.route = listOf(
                room1.id.value,
                room2.id.value,
                room3.id.value
            )
        }

        controller.move()

        assertThat(test.getRoomForMob(mob).id).isEqualTo(room2.id)

        controller.move()

        assertThat(test.getRoomForMob(mob).id).isEqualTo(room3.id)

        controller.move()

        assertThat(test.getRoomForMob(mob).id).isEqualTo(room2.id)

        controller.move()

        assertThat(test.getRoomForMob(mob).id).isEqualTo(room1.id)
    }

    @Test
    fun willNotWanderOutOfArea() {
        // setup
        val test = createTestService()
        val mobs = test.getMobRooms()
            .filter { it.mob.id.value == 9 }
            .map { it.mob }
        val prob = ProbabilityTest(1000)
        val area = "warehouse"

        while (prob.isIterating()) {
            mobs.forEach {
                test.createMobController(it).move()
            }
            val areas = mobs.map { test.getRoomForMob(it).area }
            prob.decrementIteration(areas.all { it == area }, true)
        }

        assertThat(prob.getOutcome1()).isEqualTo(1000)
    }
}
