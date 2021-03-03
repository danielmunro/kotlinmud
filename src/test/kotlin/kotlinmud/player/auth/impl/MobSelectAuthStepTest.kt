package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.service.TestService
import kotlinmud.test.helper.createTestService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MobSelectAuthStepTest {
    @Test
    fun testCanSelectOwnMob() {
        // setup
        val test = createTestService()
        val player = test.createPlayer(emailAddress)
        test.loginClientAsPlayer(test.getClient(), player)

        // given
        setPreAuth(test)
        val mob = test.createPlayerMob("foo", player)

        // when
        val response = runBlocking { test.runPreAuth(mob.name) }

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCannotSelectOtherPlayerMob() {
        // setup
        val test = createTestService()
        val p1 = test.createPlayer(emailAddress)
        val p2 = test.createPlayer("p2@danmunro.com")

        // given
        test.createPlayerMob("foo", p1)
        val mob2 = test.createPlayerMob("bar", p2)
        setPreAuth(test)

        // when
        val response = runBlocking { test.runPreAuth(mob2.name) }

        // then
        assertThat(response.message).isEqualTo("either that name is invalid or unavailable")
    }

    @Test
    fun testCanCreateNewMob() {
        // setup
        val test = createTestService()
        test.createPlayer(emailAddress)
        setPreAuth(test)

        // when
        val response = runBlocking { test.runPreAuth("foobar") }

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    private fun setPreAuth(test: TestService) {
        test.setPreAuth { authStepService, player -> MobSelectAuthStep(authStepService, player) }
    }
}
