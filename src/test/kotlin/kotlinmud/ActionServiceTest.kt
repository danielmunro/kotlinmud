package kotlinmud

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinmud.action.actions.describeRoom
import kotlinmud.io.Request
import kotlinmud.mob.Disposition
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import kotlinmud.test.globalSetup
import kotlinmud.test.globalTeardown
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass

class ActionServiceTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            globalSetup()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            globalTeardown()
        }
    }

    @Test
    fun testLookDescribesARoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)
        val observers = testService.getMobsForRoom(room).filter { it != mob }

        // when
        val response = testService.runAction(mob, "look")

        // then
        assertEquals(
            describeRoom(Request(mob, "look", room), observers),
            response.message.toActionCreator)
    }

    @Test
    fun testCanLookAtItemInRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)
        val item = transaction { room.inventory.items.first() }

        // when
        val response = testService.runAction(mob, "look ${getIdentifyingWord(item)}")

        // then
        assertEquals(
            "${item.name} is here.",
            response.message.toActionCreator)
    }

    @Test
    fun testCanLookAtItemInInventory() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val item = testService.createItem(transaction { mob.inventory })

        // when
        val response = testService.runAction(mob, "look ${getIdentifyingWord(item)}")

        // then
        assertEquals(
            "${item.name} is here.",
            response.message.toActionCreator)
    }

    @Test
    fun testMobMovesNorth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "n")

        // then
        assertEquals("test room no. 2\n" +
                "a test room is here\n" +
                "Exits [S]", response.message.toActionCreator)
    }

    @Test
    fun testMobMovesSouth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "s")

        // then
        assertEquals("test room no. 3\n" +
                "a test room is here\n" +
                "Exits [N]", response.message.toActionCreator)
    }

    @Test
    fun testMobCannotMoveInAnInvalidDirection() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "w")

        // then
        assertEquals("Alas, that direction does not exist.", response.message.toActionCreator)
    }

    @Test
    fun testMobCannotMoveWhileSitting() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        transaction { mob.disposition = Disposition.SITTING.value }

        // when
        val response = testService.runAction(mob, "n")

        // then
        assertEquals("you are sitting and cannot do that.", response.message.toActionCreator)
    }

    @Test
    fun testMobCanGetItemFromRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val room = testService.getRoomForMob(mob)
        val item = transaction { room.inventory.items.first() }

        // when
        val response = testService.runAction(mob, "get ${getIdentifyingWord(item)}")

        // then
        assertTrue(response.message.toActionCreator.startsWith("you pick up the"))
        assertEquals(1, transaction { mob.inventory.items.count() })
    }

    @Test
    fun testMobCannotGetNonexistentItemFromRoom() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "get foo")

        // then
        assertTrue(response.message.toActionCreator == "you don't see that anywhere.", response.message.toActionCreator)
        assertEquals(0, transaction { mob.inventory.items.count() })
    }

    @Test
    fun testMobCanDropItem() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val item = testService.createItem(transaction { mob.inventory })

        // when
        val response = testService.runAction(mob, "drop ${getIdentifyingWord(item)}")

        // then
        assertTrue(response.message.toActionCreator.startsWith("you drop the "))
        assertEquals(0, transaction { mob.inventory.items.count() })
    }
}
