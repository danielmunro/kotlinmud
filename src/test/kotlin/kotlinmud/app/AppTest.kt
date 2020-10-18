package kotlinmud.app

import kotlinmud.test.createTestService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AppTest {
    @Test
    fun testAppCanStartAndRunALoop() {
        // setup
        createTestService()
        val app = createApp(0)

        // expect
        runBlocking { app.loop() }
    }
}
