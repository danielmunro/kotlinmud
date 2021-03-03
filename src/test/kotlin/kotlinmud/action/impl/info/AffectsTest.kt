package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.impl.BlessAffect
import kotlinmud.affect.impl.BlindAffect
import kotlinmud.affect.impl.InvisibilityAffect
import kotlinmud.test.helper.createTestService
import org.junit.Test

class AffectsTest {
    @Test
    fun testAffectsDisplay() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMobBuilder()
            .affects(
                listOf(
                    InvisibilityAffect().createInstance(5),
                    BlessAffect().createInstance(10),
                    BlindAffect().createInstance(1),
                )
            )
            .build()

        // when
        val response = test.runAction(mob, "affects")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You are affected by:\ninvisibility: 5 ticks\nbless: 10 ticks\nblind: 1 tick\n")
    }
}
