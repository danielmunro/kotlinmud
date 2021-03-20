package kotlinmud.action.impl.player

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class DescriptionTest {
    @Test
    fun testPlayerCanSetDescription() {
        // setup
        val test = createTestService()
        val descriptionString = "this is my new description."
        val mob = test.createPlayerMob()

        val response = test.runAction("description $descriptionString")

        assertThat(response.message.toActionCreator).isEqualTo("Your description is updated.")
        assertThat(mob.description).isEqualTo(descriptionString)
    }
}
