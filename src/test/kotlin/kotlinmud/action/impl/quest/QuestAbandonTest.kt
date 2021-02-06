package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.helper.createQuestEntity
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
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
        val quest = test.findQuest(QuestType.JOIN_PRAETORIAN_GUARD)!!

        // given
        createQuestEntity(transaction { mob.mobCard!! }, quest.type)
        val count = transaction { mob.mobCard!!.quests.count() }

        // when
        val response = test.runAction("quest abandon ${getIdentifyingWord(quest)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you abandon the quest: `${quest.name}`")
        assertThat(transaction { mob.mobCard!!.quests.count() }).isEqualTo(count - 1)
    }
}
