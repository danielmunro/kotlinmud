package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class LookTest {
    @Test
    fun testLookDescribesARoom() {
        // setup
        val testService = createTestService()

        // when
        val response = testService.runAction("look")

        // then
        assertThat(response.message.toActionCreator).contains("Exits [")
    }

    @Test
    fun testLookDescribesDirections() {
        // setup
        val testService = createTestService()
        val startRoom = testService.getStartRoom()
        testService.createRoom {
            it.north = startRoom
            startRoom.south = it
        }
        testService.createRoom {
            it.south = startRoom
            startRoom.north = it
        }

        // when
        val response = testService.runAction("look")

        // then
        assertThat(response.message.toActionCreator).contains("[NS]")
    }

    @Test
    fun testCannotSeeRoomWhenBlind() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        mob.affects.add(createAffect(AffectType.BLIND))

        // when
        val response = testService.runAction("look")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't see anything, you're blind!")
    }

    @Test
    fun testCanLookAtItemInRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = mob.room
        val item = testService.createItem()

        // given
        room.items.add(item)

        // when
        val response = testService.runAction("look ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(item.description)
    }

    @Test
    fun testCanLookAtItemInInventory() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val item = testService.createItem()

        // given
        mob.items.add(item)

        // when
        val response = testService.runAction("look ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(item.description)
    }

    @Test
    fun testCanLookAtMobInRoom() {
        // setup
        val testService = createTestService()
        testService.createMob()
        val mob2 = testService.createMob()

        // when
        val response = testService.runAction("look ${getIdentifyingWord(mob2)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(mob2.description)
    }

    @Test
    fun testCanSeeADoor() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = mob.room

        // given
        val door = transaction {
            room.northDoor = DoorDAO.new {
                name = "a door"
                description = "a door"
                disposition = DoorDisposition.OPEN
                defaultDisposition = DoorDisposition.OPEN
            }
            room.northDoor!!
        }

        // when
        val response = testService.runAction("look")

        // then
        assertThat(response.message.toActionCreator).contains(door.name)
    }

    @Test
    fun testCannotSeeInvisibleMobs() {
        // setup
        val testService = createTestService()

        // given
        testService.createMob()
        val mob = testService
            .createMobBuilder()
            .affects(listOf(createAffect(AffectType.INVISIBILITY)))
            .build()

        // when
        val response = testService.runAction("look")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(mob.name)
    }
}
