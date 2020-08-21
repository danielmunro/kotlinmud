package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.impl.BlessAffect
import kotlinmud.affect.impl.BlindAffect
import kotlinmud.affect.impl.InvisibilityAffect
import kotlinmud.test.createTestService
import org.junit.Test

class AffectsTest {
    @Test
    fun testAffectsDisplay() {
        // setup
        val test = createTestService()

        // given
        test.createMob {
            InvisibilityAffect().createInstance(5).mob = it
            BlessAffect().createInstance(10).mob = it
            BlindAffect().createInstance(1).mob = it
        }

        // when
        val response = test.runAction("affects")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You are affected by:\ninvisibility: 5 ticks\nbless: 10 ticks\nblind: 1 tick\n")
    }
}
