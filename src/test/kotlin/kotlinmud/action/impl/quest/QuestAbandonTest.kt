package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.quest.helper.createQuestEntity
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class QuestAbandonTest {
    @Test
    fun testCanAbandonAQuest() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMob()
        test.createQuestGiver()
        val quest = test.findQuest(QuestType.JOIN_PRAETORIAN_GUARD)!!

        // given
        mob.quests[quest.type] = QuestStatus.INITIALIZED
        val count = mob.quests.size

        // when
        val response = test.runAction("quest abandon ${getIdentifyingWord(quest)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you abandon the quest: `${quest.name}`")
        assertThat(mob.quests).hasSize(count - 1)
    }
}
