package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.test.helper.createTestService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MobControllerTest {
    @Test
    fun testCanWalkOnDefinedPath() {
        // setup
        val test = createTestService()
        val room1 = test.getStartRoom()
        val room2 = test.createRoom()
        val room3 = test.createRoom()
        val mob = test.createMobBuilder()
            .job(JobType.PATROL)
            .route(
                listOf(
                    room1,
                    room2,
                    room3,
                )
            )
            .build()
        val controller = test.createMobController(mob)
        room1.east = room2
        room2.west = room1
        room2.south = room3
        room3.north = room2

        runBlocking { controller.move() }

        assertThat(mob.room).isEqualTo(room2)

        runBlocking { controller.move() }

        assertThat(mob.room).isEqualTo(room3)

        runBlocking { controller.move() }

        assertThat(mob.room).isEqualTo(room2)

        runBlocking { controller.move() }

        assertThat(mob.room).isEqualTo(room1)

        runBlocking { controller.move() }

        assertThat(mob.room).isEqualTo(room2)
    }
}
