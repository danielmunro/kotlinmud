package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.model.QuestProgress
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.test.helper.createTestService
import org.junit.Test

const val responseMessage =
"""you submit the quest: `join the praetorian guard`
Your reward:
10 silver
200 experience
100 points with the Praetorian Guard """

class QuestSubmitTest {
    @Test
    fun testCanSubmitQuest() {
        // setup
        val test = createTestService()
        val quest = test.findQuest(QuestType.FindPraetorianGuardRecruiter)!!

        // given
        test.createPlayerMob {
            it.quests[quest.type] = QuestProgress().also { quest -> quest.status = QuestStatus.SATISFIED }
            it.room = test.findRoom { room -> room.id == 10 }!!
        }

        // when
        val response = test.runAction("quest submit join")

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
            it.room = test.findRoom { room -> room.id == 10 }!!
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
            it.room = test.findRoom { room -> room.id == 10 }!!
        }

        // when
        val response = test.runAction("quest submit")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't find that quest.")
    }
}
