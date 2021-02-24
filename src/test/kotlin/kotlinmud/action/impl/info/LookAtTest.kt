package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class LookAtTest {
    @Test
    fun testCannotLookAtInvisibleMobs() {
        // setup
        val testService = createTestService()

        // given
        testService.createMob()
        val mob = testService
            .createMobBuilder()
            .affects(listOf(createAffect(AffectType.INVISIBILITY)))
            .build()

        // when
        val response = testService.runAction("look ${getIdentifyingWord(mob)}")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(mob.name)
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testCannotLookAtInvisibleItems() {
        // setup
        val testService = createTestService()

        // given
        val item = testService.createItem { createAffect(AffectType.INVISIBILITY).item = it }
        testService
            .createMobBuilder()
            .items(listOf(item))
            .build()

        // when
        val response = testService.runAction("look ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(item.name)
    }
}
