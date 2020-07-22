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
        val src = testService.getStartRoom()
        val dst = testService.createRoom()

        // given
        transaction { src.north = dst }

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
        val src = testService.getStartRoom()
        val dst = testService.createRoom()

        // given
        transaction { src.south = dst }

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
        val src = testService.getStartRoom()
        val dst = testService.createRoom()

        // given
        transaction { src.east = dst }

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
        val src = testService.getStartRoom()
        val dst = testService.createRoom()

        // given
        transaction { src.west = dst }

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
        val src = testService.getStartRoom()
        val dst = testService.createRoom()

        // given
        transaction { src.up = dst }

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
        val src = testService.getStartRoom()
        val dst = testService.createRoom()

        // given
        transaction { src.down = dst }

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
        val mob = testService.createMob()

        // given
        transaction { mob.disposition = Disposition.SITTING }

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
        val src = testService.getStartRoom()
        val dst = testService.createRoom()

        // given
        transaction {
            src.west = dst
            src.westDoor = DoorDAO.new {
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
        val src = test.getStartRoom()
        val dst = test.createRoom()

        // expect
        assertThat(mob.hp).isEqualTo(mob.calc(Attribute.HP))

        // given
        transaction {
            src.west = dst
            dst.elevation = src.elevation + 5
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
        val src = test.getStartRoom()
        val dst = test.createRoom()

        // expect
        assertThat(mob.hp).isEqualTo(mob.calc(Attribute.HP))

        // given
        transaction {
            src.west = dst
            dst.elevation = src.elevation - 5
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
        val src = test.getStartRoom()
        val dst = test.createRoom()

        // given
        transaction {
            src.north = dst
            dst.substrate = SubstrateType.DIRT
        }

        // when
        val response = test.runAction(mob, "n")

        // then
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
        assertThat(response.message.toActionCreator).isEqualTo("${transaction { dst.name } } is blocked by dirt.")
    }
}
