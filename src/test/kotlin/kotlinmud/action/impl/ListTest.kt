package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import org.junit.Test

class ListTest {
    @Test
    fun testListSanity() {
        // setup
        val test = createTestService()
        test.respawnWorld()
        val mob = test.createMob()

        // given
        test.runAction(mob, "s")
        test.runAction(mob, "w")

        // when
        val response = test.runAction(mob, "list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
"""
[lvl cost name]
   1   25 a practice shield
   1   25 a practice sword
   1   25 a practice mace
   5   25 a leather girdle
   5   25 a leather bracer
   5   25 a pair of leather gloves
   5   25 a pair of leather sleeves
   5   25 a pair of leather boots
   5   25 a pair of leather bracers
   5   25 a pair of leather pants""".trimMargin())
    }

    @Test
    fun testListSanity2() {
        // setup
        val test = createTestService()
        test.respawnWorld()
        val mob = test.createMob()

        // given
        test.runAction(mob, "s")
        test.runAction(mob, "e")

        // when
        val response = test.runAction(mob, "list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
"""
[lvl cost name]
   1   10 a loaf of bread
   1   20 a meat pie
   1    8 a slice of flat bread
   1   20 a bottle of ale""".trimMargin())
    }
}
