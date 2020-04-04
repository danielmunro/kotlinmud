package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlinmud.affect.AffectType
import kotlinmud.affect.affects
import kotlinmud.io.IOStatus
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class LookAtTest {
    @Test
    fun testCannotLookAtInvisibleMobs() {
        // setup
        val testService = createTestService()

        // given
        val mob1 = testService.withMob {
            it.affects(affects(AffectType.INVISIBILITY))
        }
        val mob2 = testService.createMob()

        // when
        val response = testService.runAction(mob2, "look ${getIdentifyingWord(mob1)}")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(mob1.name)
        assertThat(response.status).isEqualTo(IOStatus.ERROR)
    }

    @Test
    fun testCannotLookAtInvisibleItems() {
        // setup
        val testService = createTestService()

        // given
        val item = testService.buildItem(
            testService.itemBuilder()
                .affects(
                    affects(AffectType.INVISIBILITY))
        )
        val mob = testService.createMob()
        mob.inventory.items.add(item)

        // when
        val response = testService.runAction(mob, "look ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(item.name)
    }
}
