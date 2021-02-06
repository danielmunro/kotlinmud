package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class QuestAbandonTest {
    @Test
    fun testCanAbandonAQuest() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMob()
        test.createMob {
            it.job = JobType.QUEST
        }

        // given
        transaction {
            QuestDAO.new {
                mobCard = mob.mobCard!!
                quest = QuestType.JOIN_PRAETORIAN_GUARD
                status = QuestStatus.INITIALIZED
            }
        }
        val count = transaction { mob.mobCard!!.quests.count() }

        // when
        val response = test.runAction("quest abandon captain")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you abandon the quest: `Talk to Captain Bartok of the Praetorian Guard`")
        assertThat(transaction { mob.mobCard!!.quests.count() }).isEqualTo(count - 1)
    }
}
