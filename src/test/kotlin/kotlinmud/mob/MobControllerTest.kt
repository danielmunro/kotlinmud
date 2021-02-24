package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class MobControllerTest {
    @Test
    fun testCanWalkOnDefinedPath() {
//        // setup
//        val test = createTestService()
//        val mob = test.createMob()
//        val controller = test.createMobController(mob)
//        val room1 = test.getStartRoom()
//        val room2 = test.createRoom()
//        val room3 = test.createRoom()
//        transaction {
//            room1.east = room2
//            room2.west = room1
//            room2.south = room3
//            room3.north = room2
//            mob.job = JobType.PATROL
//            mob.route = listOf(
//                room1.id.value,
//                room2.id.value,
//                room3.id.value
//            )
//        }
//
//        runBlocking { controller.move() }
//
//        assertThat(transaction { mob.room }.id).isEqualTo(room2.id)
//
//        runBlocking { controller.move() }
//
//        assertThat(transaction { mob.room }.id).isEqualTo(room3.id)
//
//        runBlocking { controller.move() }
//
//        assertThat(transaction { mob.room }.id).isEqualTo(room2.id)
//
//        runBlocking { controller.move() }
//
//        assertThat(transaction { mob.room }.id).isEqualTo(room1.id)
    }
}
