package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.isEqualTo
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
}
