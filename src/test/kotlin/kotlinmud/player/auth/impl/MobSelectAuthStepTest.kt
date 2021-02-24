package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.TestService
import kotlinmud.test.createTestServiceWithResetDB
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MobSelectAuthStepTest {
    @Test
    fun testCanSelectOwnMob() {
        // setup
        val test = createTestServiceWithResetDB()
        test.createPlayer(emailAddress)

        // given
        setPreAuth(test)
        val mob = test.createPlayerMob()

        // when
        val response = runBlocking { test.runPreAuth(mob.name) }

        // then
        assertThat(response.message).isEqualTo("ok.")
    }

    @Test
    fun testCannotSelectOtherPlayerMob() {
//        // setup
//        val test = createTestServiceWithResetDB()
//        val p1 = test.createPlayer(emailAddress)
//        val p2 = test.createPlayer("p2@danmunro.com")
//
//        val mob1 = test.createPlayerMob()
//        val mob2 = test.createPlayerMob()
//
//        // given
//        transaction {
//            mob1.name = "foo"
//            mob1.player = p1
//            mob2.name = "bar"
//            mob2.player = p2
//        }
//        setPreAuth(test)
//
//        // when
//        val response = runBlocking { test.runPreAuth(mob2.name) }
//
//        // then
//        assertThat(response.message).isEqualTo("either that name is invalid or unavailable")
    }

    @Test
    fun testCanCreateNewMob() {
        // setup
        val test = createTestServiceWithResetDB()
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
