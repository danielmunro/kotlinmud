package kotlinmud.player.service

import assertk.assertThat
import assertk.assertions.isInstanceOf
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.test.helper.createTestService
import org.junit.Test

class PlayerServiceTest {
    @Test
    fun testCreateNewPlayerWorksWithValidEmail() {
        // setup
        val test = createTestService()

        // expect
        assertThat(test.createPlayer("foo@bar.com")).isInstanceOf(PlayerDAO::class)
    }
}
