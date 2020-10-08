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
            sticks
            torch
            glass block
            bed
            a sleeping bag
            shelter
            wooden sword
            wooden axe
            wooden pickaxe
            stone axe
            stone pickaxe
            stone sword
            iron sword
            iron pickaxe
            stone axe
        """.trimIndent())
    }
}
