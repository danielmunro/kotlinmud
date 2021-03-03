package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class RecipeOfTest {
    @Test
    fun testRecipeOfCanDisplayForSleepingBag() {
        // setup
        val test = createTestService()

        // when
        val response = test.runAction("recipe of sleeping")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
            """
            Recipe for a sleeping bag:
            (4) wool
            """.trimIndent()
        )
    }
}
