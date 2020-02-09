/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package kotlinmud

import kotlinmud.test.createTestService
import kotlinmud.test.globalSetup
import kotlinmud.test.globalTeardown
import org.junit.AfterClass
import org.junit.BeforeClass
import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun testMobMovesNorth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "n")

        // then
        assertEquals(response.message, "a test room 2\n" +
                "a test room is here\n" +
                "Exits [S]")
    }

    @Test
    fun testMobMovesSouth() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "s")

        // then
        assertEquals(response.message, "a test room 3\n" +
                "a test room is here\n" +
                "Exits [N]")
    }

    @Test
    fun testMobCannotMoveInAnInvalidDirection() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.runAction(mob, "w")

        // then
        assertEquals(response.message, "Alas, that direction does not exist.")
    }
}
