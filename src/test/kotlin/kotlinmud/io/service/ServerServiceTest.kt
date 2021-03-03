package kotlinmud.io.service

import kotlinmud.test.helper.createTestService
import org.junit.Test

class ServerServiceTest {
    @Test
    fun testReadIntoBuffersIsNonBlocking() {
        // setup
        val test = createTestService()

        // when
        test.createClient()

        // then -- don't block
        test.readIntoBuffers()
    }
}
