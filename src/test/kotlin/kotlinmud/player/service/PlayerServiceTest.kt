package kotlinmud.player.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import org.junit.Test

class PlayerServiceTest {
    @Test
    fun testCanFindMobCardByName() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMobBuilder().build()
        val mob = test.createPlayerMobBuilder().build()
        test.createPlayerMobBuilder().build()

        // when
        val result = test.findMobCardByName(mob.name)

        // then
        assertThat(result!!.mobName).isEqualTo(mob.name)
    }

    @Test
    fun testCanFindPlayerByOTP() {
        // setup
        val test = createTestService()
        test.createPlayer()
        val player = test.createPlayer()
        test.createPlayer()

        // given
        player.lastOTP = "foo"

        // when
        val result = test.findPlayerByOTP("foo")

        assertThat(result!!.email).isEqualTo(player.email)
    }
}
