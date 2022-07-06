package kotlinmud.action.impl.admin.area

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class AddTest {
    @Test
    fun testCanAddArea() {
        // setup
        val test = createTestService()

        // when
        val response = test.runActionAsAdmin("area add The Dark Forest")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("The Dark Forest added")
    }
}
