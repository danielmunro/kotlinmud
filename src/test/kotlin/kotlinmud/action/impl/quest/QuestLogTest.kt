package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.model.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class QuestLogTest {
    @Test
    fun testQuestLogSanity() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMob()
        val quest1 = test.findQuest(QuestType.JOIN_PRAETORIAN_GUARD)!!
        val quest2 = test.findQuest(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER)!!

        // given
        mob.quests[quest1.type] = Quest()
        mob.quests[quest2.type] = Quest()

        // when
        val response = test.runAction("quest log")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("${quest1.name}\n, ${quest2.name}\n")
    }
}
