package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.attributes.type.Attribute
import kotlinmud.biome.type.SubstrateType
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.type.Disposition
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.helper.createTestService
import org.junit.Test

class MoveTest {
    @Test
    fun testMobMovesNorth() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()
        val mob = testService.createMob()

        // given
        testService.getStartRoom { it.north = dst }

        // when
        testService.runAction(mob, "n")

        // then
        assertThat(mob.room).isEqualTo(dst)
    }

    @Test
    fun testMobMovesSouth() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()
        val mob = testService.createMob()

        // given
        testService.getStartRoom { it.south = dst }

        // when
        testService.runAction(mob, "s")

        // then
        assertThat(mob.room).isEqualTo(dst)
    }

    @Test
    fun testMobMovesEast() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()
        val mob = testService.createMob()

        // given
        testService.getStartRoom { it.east = dst }

        // when
        testService.runAction(mob, "e")

        // then
        assertThat(mob.room).isEqualTo(dst)
    }

    @Test
    fun testMobMovesWest() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()
        val mob = testService.createMob()

        // given
        testService.getStartRoom { it.west = dst }

        // when
        testService.runAction(mob, "w")

        // then
        assertThat(mob.room).isEqualTo(dst)
    }

    @Test
    fun testMobMovesUp() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()
        val mob = testService.createMob()

        // given
        testService.getStartRoom { it.up = dst }

        // when
        testService.runAction(mob, "u")

        // then
        assertThat(mob.room).isEqualTo(dst)
    }

    @Test
    fun testMobMovesDown() {
        // setup
        val testService = createTestService()
        val dst = testService.createRoom()
        val mob = testService.createMob()

        // given
        testService.getStartRoom { it.down = dst }

        // when
        testService.runAction(mob, "d")

        // then
        assertThat(mob.room).isEqualTo(dst)
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
            it.westDoor = testService.createDoor()
            it.westDoor!!.disposition = DoorDisposition.CLOSED
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
            .also { it.elevation = 5 }
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
        val mob = test.createMob()
        val dst = test.createRoomBuilder()
            .also { it.elevation = 6 }
            .build()
        mob.room = dst

        // given
        test.getStartRoom {
            it.west = dst
            dst.east = it
        }

        // when
        test.runAction("east")

        // then
        assertThat(mob.hp).isLessThan(mob.calc(Attribute.HP))
    }

    @Test
    fun testMobCannotMoveIntoSolidSubstrates() {
        // setup
        val test = createTestService()
        val dst = test.createRoomBuilder()
            .also { it.substrate = SubstrateType.DIRT }
            .build()

        // given
        test.getStartRoom {
            it.north = dst
        }

        // when
        val response = test.runAction("n")

        // then
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
        assertThat(response.message.toActionCreator).isEqualTo("${dst.name} is blocked by dirt.")
    }
}
