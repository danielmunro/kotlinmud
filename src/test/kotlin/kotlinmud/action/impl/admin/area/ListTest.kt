package kotlinmud.action.impl.admin.area

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class ListTest {
    @Test
    fun testCanListAreas() {
        // setup
        val test = createTestService(hydrate = false)

        val response = test.runActionAsAdmin("area list")

        assertThat(response.message.toActionCreator).isEqualTo("Areas:\n\nTest")
    }
}
