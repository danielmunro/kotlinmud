package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.action.impl.info.describeRoom
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
        val mob = testService.createMob()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.north = dst }

        // when
        val response = testService.runAction(mob, "n")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(dst, mob, testService.getMobsForRoom(dst), testService.findAllItemsByOwner(dst))
        )
    }

    @Test
    fun testMobMovesSouth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.south = dst }

        // when
        val response = testService.runAction(mob, "s")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(dst, mob, testService.getMobsForRoom(dst), testService.findAllItemsByOwner(dst))
        )
    }

    @Test
    fun testMobMovesEast() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.east = dst }

        // when
        val response = testService.runAction(mob, "e")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(dst, mob, testService.getMobsForRoom(dst), testService.findAllItemsByOwner(dst))
        )
    }

    @Test
    fun testMobMovesWest() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.west = dst }

        // when
        val response = testService.runAction(mob, "w")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(dst, mob, testService.getMobsForRoom(dst), testService.findAllItemsByOwner(dst))
        )
    }

    @Test
    fun testMobMovesUp() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.up = dst }

        // when
        val response = testService.runAction(mob, "u")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(dst, mob, testService.getMobsForRoom(dst), testService.findAllItemsByOwner(dst))
        )
    }

    @Test
    fun testMobMovesDown() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val dst = testService.createRoom()

        // given
        testService.getStartRoom { it.down = dst }

        // when
        val response = testService.runAction(mob, "d")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(dst, mob, testService.getMobsForRoom(dst), testService.findAllItemsByOwner(dst))
        )
    }

    @Test
    fun testMobCannotMoveInAnInvalidDirection() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "u")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Alas, that direction does not exist.")
    }

    @Test
    fun testMobCannotMoveWhileSitting() {
        // setup
        val testService = createTestService()

        // given
        val mob = testService.createMob { it.disposition = Disposition.SITTING }

        // when
        val response = testService.runAction(mob, "n")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you are sitting and cannot do that.")
    }

    @Test
    fun testMobCannotMoveDirectionIfDoorIsClosed() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
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
        val response = testService.runAction(mob, "w")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you must open the door first.")
    }

    @Test
    fun testMobCannotMoveOverGreatPositiveElevations() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom()

        // expect
        assertThat(mob.hp).isEqualTo(mob.calc(Attribute.HP))

        // given
        test.getStartRoom {
            it.west = dst
            dst.elevation = it.elevation + 5
        }

        // when
        val response = test.runAction(mob, "west")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't climb that elevation.")
    }

    @Test
    fun testMobTakesDamageWhenFalling() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom()

        // expect
        assertThat(mob.hp).isEqualTo(mob.calc(Attribute.HP))

        // given
        test.getStartRoom {
            it.west = dst
            it.elevation = dst.elevation + 5
        }

        // when
        test.runAction(mob, "west")

        // then
        assertThat(mob.hp).isLessThan(mob.calc(Attribute.HP))
    }

    @Test
    fun testMobCannotMoveIntoSolidSubstrates() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom()

        // given
        test.getStartRoom {
            it.north = dst
            dst.substrate = SubstrateType.DIRT
        }

        // when
        val response = test.runAction(mob, "n")

        // then
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
        assertThat(response.message.toActionCreator).isEqualTo("${transaction { dst.name } } is blocked by dirt.")
    }
}
