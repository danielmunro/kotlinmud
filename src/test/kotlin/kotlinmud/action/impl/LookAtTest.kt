package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.doesNotContain
import kotlinmud.affect.AffectType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class LookAtTest {
    @Test
    fun testCannotLookAtInvisibleMobs() {
        // setup
        val testService = createTestService()

        // given
        val mob1 = testService.buildMob(
            testService.mobBuilder()
                .addAffect(AffectType.INVISIBLE)
        )
        val mob2 = testService.createMob()

        // when
        val response = testService.runAction(mob2, "look ${getIdentifyingWord(mob1)}")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(mob1.name)
    }
}
