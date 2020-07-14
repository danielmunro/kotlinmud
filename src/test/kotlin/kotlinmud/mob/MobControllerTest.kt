package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.junit.Test

class MobControllerTest {
    @Test
    fun testCanWalkOnDefinedPath() {
        // setup
        val test = createTestService()
        test.respawnWorld()
        val mob = test.getMobRooms().find { it.mob.id.value == 11 }!!.mob

        val controller = test.createMobController(mob)

        controller.move()

        assertThat(test.getRoomForMob(mob).id.value).isEqualTo(2)

        controller.move()

        assertThat(test.getRoomForMob(mob).id.value).isEqualTo(1)

        controller.move()

        assertThat(test.getRoomForMob(mob).id.value).isEqualTo(100)

        controller.move()

        assertThat(test.getRoomForMob(mob).id.value).isEqualTo(1)
    }

    @Test
    fun willNotWanderOutOfArea() {
        // setup
        val test = createTestService()
        test.respawnWorld()
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
