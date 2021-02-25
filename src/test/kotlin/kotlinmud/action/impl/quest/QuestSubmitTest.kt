package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.helper.createQuestEntity
import kotlinmud.quest.type.QuestType
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.type.CanonicalId
import org.junit.Test

class QuestSubmitTest {
    @Test
    fun testCanSubmitQuest() {
        // setup
        val test = createTestServiceWithResetDB()
        val quest = test.findQuest(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER)!!

        // given
        test.createPlayerMob {
            createQuestEntity(it.mobCard!!, quest.type)
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
        }

        // when
        val response = test.runAction("quest submit re")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you submit the quest: `${quest.name}`")
    }

    @Test
    fun testDoesNotErrorOutWhenAQuestIsNotSpecified() {
        // setup
        val test = createTestServiceWithResetDB()
        val quest = test.findQuest(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER)!!

        // given
        test.createPlayerMob {
            createQuestEntity(it.mobCard!!, quest.type)
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
        }

        // when
        val response = test.runAction("quest submit")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you submit the quest: `${quest.name}`")
    }

    @Test
    fun testQuestMustBeAcceptedToBeSubmitted() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
        }

        // when
        val response = test.runAction("quest submit")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't find that quest.")
    }
}
