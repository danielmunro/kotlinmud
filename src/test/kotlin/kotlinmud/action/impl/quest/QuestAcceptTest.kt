package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.helper.createQuestEntity
import kotlinmud.quest.type.QuestType
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class QuestAcceptTest {
    @Test
    fun testCanAcceptAQuest() {
        // setup
        val test = createTestService()
        val quest = test.findQuest(QuestType.JOIN_PRAETORIAN_GUARD)!!

        // given
        val mob = test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.FIND_PRAETORIAN_CAPTAIN)
        }
        val count = transaction { mob.mobCard!!.quests.count() }

        // when
        val response = test.runAction("quest accept ${getIdentifyingWord(quest)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you accept the quest: `${quest.name}`")
        assertThat(transaction { mob.mobCard!!.quests.count() }).isEqualTo(count + 1)
    }

    @Test
    fun testAcceptRequiresAQuestGiver() {
        // setup
        val test = createTestService()

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see a quest giver here.")
    }

    @Test
    fun testCannotAcceptAQuestTwice() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.FIND_PRAETORIAN_CAPTAIN)
        }

        // given
        createQuestEntity(transaction { mob.mobCard!! }, QuestType.FIND_CAPTAIN_BARTOK_PRAETORIANS)

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("they cannot grant you that.")
    }
}
