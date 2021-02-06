package kotlinmud.app

import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AppTest {
    @Test
    fun testAppCanStartAndRunALoop() {
        // setup
        createTestServiceWithResetDB()
        val app = createApp(0)

        // expect
        runBlocking { app.loop() }
    }
}
