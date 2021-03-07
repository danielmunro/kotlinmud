package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isFailure
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.test.helper.createTestService
import org.junit.Test

class CompleteAuthStepTest {
    @Test
    fun testSanity() {
        // setup
        val test = createTestService()
        val playerMob = test.createPlayerMob()

        // given
        val authStep = CompleteAuthStep(playerMob)

        // expect
        assertThat { authStep.handlePreAuthRequest(PreAuthRequest(test.createClient(), "foo")) }.isFailure()
        assertThat { authStep.getNextAuthStep() }.isFailure()
    }
}
