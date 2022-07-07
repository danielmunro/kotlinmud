package kotlinmud.action.impl.admin.area

import assertk.assertThat
import assertk.assertions.startsWith
import kotlinmud.test.helper.createTestService
import org.junit.Test

class ListTest {
    @Test
    fun testCanListAreas() {
        // setup
        val test = createTestService(hydrate = false)

        val response = test.runActionAsAdmin("area list")

        assertThat(response.message.toActionCreator).startsWith("Areas:\n\n")
    }
}
