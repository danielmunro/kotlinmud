package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import org.junit.Test

class RecipesTest {
    @Test
    fun testRecipesCanDisplay() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // when
        val response = test.runAction(mob, "recipes")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("""
            Recipes:

            a builder's table
            lumber
            shelter
            sticks
            torch
            a sleeping bag
        """.trimIndent())
    }
}
