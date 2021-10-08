package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.model.QuestProgress
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.test.helper.createTestService
import kotlinmud.type.RoomCanonicalId
import org.junit.Test

const val responseMessage =
"""you submit the quest: `find a recruiter for the Praetorian Guard`
Your reward:
100 points with the Praetorian Guard
1000 experience
1 gold
15 silver """

class QuestSubmitTest {
    @Test
    fun testCanSubmitQuest() {
        // setup
        val test = createTestService()
        val quest = test.findQuest(QuestType.FindPraetorianGuardRecruiter)!!

        // given
        test.createPlayerMob {
            it.quests[quest.type] = QuestProgress().also { quest -> quest.status = QuestStatus.SATISFIED }
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.PraetorianGuardRecruiterFound }!!
        }

        // when
        val response = test.runAction("quest submit re")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(responseMessage)
    }

    @Test
    fun testDoesNotErrorOutWhenAQuestIsNotSpecified() {
        // setup
        val test = createTestService()
        val quest = test.findQuest(QuestType.FindPraetorianGuardRecruiter)!!

        // given
        test.createPlayerMob {
            it.quests[quest.type] = QuestProgress().also { quest -> quest.status = QuestStatus.SATISFIED }
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.PraetorianGuardRecruiterFound }!!
        }

        // when
        val response = test.runAction("quest submit")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(responseMessage)
    }

    @Test
    fun testQuestMustBeAcceptedToBeSubmitted() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMob {
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.PraetorianGuardRecruiterFound }!!
        }

        // when
        val response = test.runAction("quest submit")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't find that quest.")
    }
}
