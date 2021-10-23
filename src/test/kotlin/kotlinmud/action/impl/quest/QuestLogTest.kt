package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.contains
import kotlinmud.quest.model.QuestProgress
import kotlinmud.quest.type.QuestType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class QuestLogTest {
    @Test
    fun testQuestLogSanity() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMob()
        val quest1 = test.findQuest(QuestType.BarbosaSupplies)!!
        val quest2 = test.findQuest(QuestType.FindPraetorianGuardRecruiter)!!

        // given
        mob.quests[quest1.type] = QuestProgress()
        mob.quests[quest2.type] = QuestProgress()

        // when
        val response = test.runAction("quest log")

        // then
        assertThat(response.message.toActionCreator).contains(quest1.name)
        assertThat(response.message.toActionCreator).contains(quest2.name)
    }
}
