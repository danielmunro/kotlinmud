package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.util.stream.Collectors
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.junit.Test

class MobControllerTest {
    @Test
    fun testCanWalkOnDefinedPath() {
        // setup
        val test = createTestService()
        test.respawnWorld()
        val mob = test.getMobRooms().find { it.mob.id == 11 }!!.mob

        val controller = test.createMobController(mob)

        controller.move()

        assertThat(test.getRoomForMob(mob)).isEqualTo(test.getRooms().find { it.id == 2 })

        controller.move()

        assertThat(test.getRoomForMob(mob)).isEqualTo(test.getRooms().find { it.id == 1 })

        controller.move()

        assertThat(test.getRoomForMob(mob)).isEqualTo(test.getRooms().find { it.id == 100 })

        controller.move()

        assertThat(test.getRoomForMob(mob)).isEqualTo(test.getRooms().find { it.id == 1 })
    }

    @Test
    fun willNotWanderOutOfArea() {
        // setup
        val test = createTestService()
        test.respawnWorld()
        val mobs = test.getMobRooms().stream().filter { it.mob.id == 9 }.map { it.mob }.collect(Collectors.toList())
        val prob = ProbabilityTest()
        val area = test.getRoomForMob(mobs.first()).area

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
