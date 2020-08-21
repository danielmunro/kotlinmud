package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class MobSelectAuthStepTest {
    @Test
    fun testCanSelectOwnMob() {
        // setup
        val test = createTestServiceWithResetDB()
        test.createPlayer(emailAddress)

        // given
        test.runPreAuth(emailAddress)
        val player = findPlayerByEmail(emailAddress)!!
        val mob = test.createPlayerMob()
        transaction { mob.player = player }
        test.runPreAuth(transaction { player.lastOTP!! })

        // when
        val response = test.runPreAuth(mob.name)

        // then
        assertThat(response.message).isEqualTo("ok.")
    }
}
