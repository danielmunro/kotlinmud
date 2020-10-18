package kotlinmud.app

import kotlinmud.test.createTestService
import org.junit.Test

class AppTest {
    @Test
    fun testAppCanStartAndRunALoop() {
        // setup
        createTestService()
        val app = createApp(0)

        // expect
        app.loop()
    }
}