package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.helper.createQuestEntity
import kotlinmud.quest.type.QuestType
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class QuestLogTest {
    @Test
    fun testQuestLogSanity() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMob()
        val mobCard = transaction { mob.mobCard!! }

        // given
        createQuestEntity(mobCard, QuestType.FIND_CAPTAIN_BARTOK_PRAETORIANS)
        createQuestEntity(mobCard, QuestType.JOIN_PRAETORIAN_GUARD)

        // when
        val response = test.runAction("quest log")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Talk to Captain Bartok of the Praetorian Guard\n, Find a recruiter for the Praetorian Guard\n")
    }
}
