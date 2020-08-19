package kotlinmud.player.auth.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlinmud.player.repository.findPlayerByEmail
import kotlinmud.player.table.Players
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class EmailAuthStepTest {
    @Test
    fun testPlayerIsCreatedWhenNotFound() {
        // setup
        val test = createTestServiceWithResetDB()

        // when
        val response = test.runPreAuth(emailAddress)

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(findPlayerByEmail(emailAddress)).isNotNull()
    }

    @Test
    fun testPlayerCanBeFound() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        test.createPlayer(emailAddress)

        // when
        val response = test.runPreAuth(emailAddress)

        // then
        assertThat(response.message).isEqualTo("ok.")
        assertThat(findPlayerByEmail(emailAddress)).isNotNull()
    }

    @Test
    fun testDoesNotDuplicatePlayerRecords() {
        // setup
        val test = createTestServiceWithResetDB()
        test.createPlayer(emailAddress)

        // given
        test.runPreAuth(emailAddress)

        // when
        val players = transaction { Players.select { Players.email eq emailAddress }.count() }

        // then
        assertThat(players).isEqualTo(1)
    }
}
