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

        // when
        val response = test.runAction("recipes")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("""
            Recipes:

            a builder's table
            lumber
            shelter
            sticks
            torch
            a sleeping bag
            wooden sword
            wooden axe
            wooden pickaxe
        """.trimIndent())
    }
}
