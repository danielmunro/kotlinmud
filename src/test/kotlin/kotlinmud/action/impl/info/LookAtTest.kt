package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.factory.affects
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class LookAtTest {
    @Test
    fun testCannotLookAtInvisibleMobs() {
        // setup
        val testService = createTestService()

        // given
        val mob1 = testService.createMob()
        transaction {
            mob1.affects.plus(AffectDAO.new {
                type = AffectType.INVISIBILITY
            })
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
        val mob = testService.createMob()
        val item = testService.createItem(mob)
        transaction {
            item.affects.plus(AffectDAO.new {
                type = AffectType.INVISIBILITY
            })
        }

        // when
        val response = testService.runAction(mob, "look ${getIdentifyingWord(item)}")

        // then
        assertThat(response.message.toActionCreator).doesNotContain(item.name)
    }
}
