package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.attributes.type.Attribute
import kotlinmud.biome.type.SubstrateType
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.type.Disposition
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class MoveTest {
    @Test
    fun testMobMovesNorth() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.north = dst }

        // when
        val response = testService.runAction("n")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testMobMovesSouth() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.south = dst }

        // when
        val response = testService.runAction("s")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testMobMovesEast() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.east = dst }

        // when
        val response = testService.runAction("e")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testMobMovesWest() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.west = dst }

        // when
        val response = testService.runAction("w")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testMobMovesUp() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.up = dst }

        // when
        val response = testService.runAction("u")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testMobMovesDown() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.down = dst }

        // when
        val response = testService.runAction("d")

        // then
        assertThat(response.status).isEqualTo(IOStatus.OK)
    }

    @Test
    fun testMobCannotMoveInAnInvalidDirection() {
        // setup
        val testService = createTestService()

        // when
        val response = testService.runAction("u")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Alas, that direction does not exist.")
    }

    @Test
    fun testMobCannotMoveWhileSitting() {
        // setup
        val testService = createTestService()

        // given
        testService.createMob { it.disposition = Disposition.SITTING }

        // when
        val response = testService.runAction("n")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you are sitting and cannot do that.")
    }

    @Test
    fun testMobCannotMoveDirectionIfDoorIsClosed() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom {
            it.west = dst
            it.westDoor = DoorDAO.new {
                name = "a door"
                description = "a door"
                defaultDisposition = DoorDisposition.CLOSED
                disposition = DoorDisposition.CLOSED
            }
        }

        // when
        val response = testService.runAction("w")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you must open the door first.")
    }

    @Test
    fun testMobCannotMoveOverGreatPositiveElevations() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoomBuilder()
            .elevation(5)
            .build()

        // expect
        assertThat(mob.hp).isEqualTo(mob.calc(Attribute.HP))

        // given
        test.getStartRoom {
            it.west = dst
        }

        // when
        val response = test.runAction("west")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't climb that elevation.")
    }

    @Test
    fun testMobTakesDamageWhenFalling() {
        // setup
        val test = createTestService()
        test.createMob()
        val dst = test.createRoomBuilder()
            .elevation(6)
            .build()

        // given
        test.getStartRoom {
            it.west = dst
        }

        // when
        test.runAction("west")

        // then
        val mob = test.getMob()
        assertThat(mob.hp).isLessThan(mob.calc(Attribute.HP))
    }

    @Test
    fun testMobCannotMoveIntoSolidSubstrates() {
        // setup
        val test = createTestService()
        val dst = test.createRoomBuilder()
            .substrate(SubstrateType.DIRT)
            .build()

        // given
        test.getStartRoom {
            it.north = dst
        }

        // when
        val response = test.runAction("n")

        // then
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
        assertThat(response.message.toActionCreator).isEqualTo("${transaction { dst.name } } is blocked by dirt.")
    }
}
