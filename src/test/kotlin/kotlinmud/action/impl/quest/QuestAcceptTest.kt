package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.test.createTestService
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class QuestAcceptTest {
    @Test
    fun testCanAcceptAQuest() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_RECRUITER_1)
        }
        val count = transaction { mob.mobCard!!.quests.count() }

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you accept the quest: `Find Recruiter Bartok for the Praetorian Guard`")
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
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_RECRUITER_1)
        }

        // given
        transaction {
            QuestDAO.new {
                mobCard = mob.mobCard!!
                quest = QuestType.JOIN_PRAETORIAN_GUARD
                status = QuestStatus.INITIALIZED
            }
        }

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("they cannot grant you that.")
    }
}
