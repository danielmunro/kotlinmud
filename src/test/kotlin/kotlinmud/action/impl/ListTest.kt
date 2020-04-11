package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import org.junit.Test

class ListTest {
    @Test
    fun testListSanity() {
        // setup
        val test = createTestService()
        test.respawnWorld()
        val mob = test.createMob()

        // given
        test.runAction(mob, "s")
        test.runAction(mob, "w")

        // when
        val response = test.runAction(mob, "list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("[lvl cost name]\n\n   1  25 a practice shield\n   1  25 a practice sword\n   1  25 a practice mace\n   5  25 a leather girdle\n   5  25 a leather bracer\n   5  25 a pair of leather gloves\n   5  25 a pair of leather sleeves\n   5  25 a pair of leather boots\n   5  25 a pair of leather bracers\n   5  25 a pair of leather pants")
    }
}
