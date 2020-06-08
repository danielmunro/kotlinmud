package kotlinmud.io.service

import kotlinmud.test.createTestService
import org.junit.Test

class ServerServiceTest {
    @Test
    fun testReadIntoBuffersIsNonBlocking() {
        val test = createTestService()
        test.createClient()
        test.readIntoBuffers()
    }
}
