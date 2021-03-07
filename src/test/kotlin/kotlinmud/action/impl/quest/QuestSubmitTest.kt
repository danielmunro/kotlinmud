package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.helper.createQuestEntity
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.test.helper.createTestService
import kotlinmud.type.RoomCanonicalId
import org.junit.Test

class QuestSubmitTest {
    @Test
    fun testCanSubmitQuest() {
        // setup
        val test = createTestService()
        val quest = test.findQuest(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER)!!

        // given
        test.createPlayerMob {
            it.quests[quest.type] = QuestStatus.SATISFIED
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND }!!
        }

        // when
        val response = test.runAction("quest submit re")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you submit the quest: `${quest.name}`")
    }

    @Test
    fun testDoesNotErrorOutWhenAQuestIsNotSpecified() {
        // setup
        val test = createTestService()
        val quest = test.findQuest(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER)!!

        // given
        test.createPlayerMob {
            it.quests[quest.type] = QuestStatus.SATISFIED
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND }!!
        }

        // when
        val response = test.runAction("quest submit")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you submit the quest: `${quest.name}`")
    }

    @Test
    fun testQuestMustBeAcceptedToBeSubmitted() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMob {
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND }!!
        }

        // when
        val response = test.runAction("quest submit")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't find that quest.")
    }
}
