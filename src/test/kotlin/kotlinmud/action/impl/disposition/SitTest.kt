package kotlinmud.action.impl.disposition

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.Disposition
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class SitTest {
    @Test
    fun testMobCanSit() {
        // setup
        val test = createTestService()
        test.createMob()

        // when
        val response = test.runAction("sit")

        // then
        val mob = test.getMob()
        assertThat(response.message.toActionCreator).isEqualTo("you sit down.")
        assertThat(response.message.toObservers).isEqualTo("$mob sits down.")
        assertThat(transaction { mob.disposition }).isEqualTo(Disposition.SITTING)
    }

    @Test
    fun testMobCannotSitWhenSitting() {
        // setup
        val test = createTestService()

        // given
        test.createMob { it.disposition = Disposition.SITTING }

        // when
        val response = test.runAction("sit")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you are sitting and cannot do that.")
    }
}
