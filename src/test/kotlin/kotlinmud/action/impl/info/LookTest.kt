package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class LookTest {
    @Test
    fun testLookDescribesARoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = transaction { mob.room }
        val observers = testService.getMobsForRoom(room).filter { it != mob }

        // when
        val response = testService.runAction(mob, "look")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            describeRoom(
                room,
                mob,
                observers,
                testService.findAllItemsByOwner(room)
            )
        )
    }

    @Test
    fun testCannotSeeRoomWhenBlind() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val affect = createAffect(AffectType.BLIND)

        // given
        transaction { affect.mob = mob }

        // when
        val response = testService.runAction(mob, "look")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't see anything, you're blind!")
    }

    @Test
    fun testCanLookAtItemInRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = transaction { mob.room }
        val item = testService.createItem()

        // given
        transaction { item.room = room }

        // when
        val response = testService.runAction(mob, "look ${getIdentifyingWord(item)}")

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
        transaction { item.mobInventory = mob }

        // when
        val response = testService.runAction(mob, "look ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(item.description)
    }

    @Test
    fun testCanLookAtMobInRoom() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val mob2 = testService.createMob()

        // when
        val response = testService.runAction(mob1, "look ${getIdentifyingWord(mob2)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(mob2.description)
    }

    @Test
    fun testCanSeeADoor() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = transaction { mob.room }

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
        val response = testService.runAction(mob, "look")

        // then
        assertThat(response.message.toActionCreator).contains(door.name)
    }

    @Test
    fun testCannotSeeInvisibleMobs() {
        // setup
        val testService = createTestService()

        // given
        val mob1 = testService.createMob()
        transaction {
            mob1.affects.plus(AffectDAO.new {
                type = AffectType.INVISIBILITY
            })
        }
        val mob2 = testService.createMob()

        // when
        val response = testService.runAction(mob2, "look")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(mob1.name)
    }
}
