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
        val quest1 = test.findQuest(QuestType.JOIN_PRAETORIAN_GUARD)!!
        val quest2 = test.findQuest(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER)!!

        // given
        createQuestEntity(mobCard, quest1.type)
        createQuestEntity(mobCard, quest2.type)

        // when
        val response = test.runAction("quest log")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("${quest1.name}\n, ${quest2.name}\n")
    }
}
