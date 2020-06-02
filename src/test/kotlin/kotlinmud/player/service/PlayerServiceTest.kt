package kotlinmud.player.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
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
        val mobCard = test.findMobCardByName(mob.name)

        // then
        assertThat(mobCard!!.mobName).isEqualTo(mob.name)
    }

    @Test
    fun testDoesNotFindMobCardThatDoesNotExist() {
        // setup
        val test = createTestService()
        test.createPlayerMobBuilder().build()

        // when
        val mobCard = test.findMobCardByName("foo12344567")

        // then
        assertThat(mobCard).isNull()
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

        // then
        assertThat(result!!.email).isEqualTo(player.email)
    }

    @Test
    fun testDoesNotFindPlayerByOTPThatDoesNotExist() {
        // setup
        val test = createTestService()
        test.createPlayer()

        // when
        val mobCard = test.findPlayerByOTP("foo")

        // then
        assertThat(mobCard).isNull()
    }
}
