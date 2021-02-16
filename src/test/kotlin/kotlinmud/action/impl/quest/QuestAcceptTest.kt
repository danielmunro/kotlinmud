package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.quest.helper.createQuestEntity
import kotlinmud.quest.type.QuestType
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.test.getIdentifyingWord
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class QuestAcceptTest {
    @Test
    fun testCanAcceptAQuest() {
        // setup
        val test = createTestServiceWithResetDB()
        val quest = test.findQuest(QuestType.JOIN_PRAETORIAN_GUARD)!!

        // given
        val mob = test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
        }
        val count = transaction { mob.mobCard!!.quests.count() }

        // when
        val response = test.runAction("quest accept ${getIdentifyingWord(quest)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you accept the quest: `${quest.name}`")
        assertThat(transaction { mob.mobCard!!.quests.count() }).isEqualTo(count + 1)
    }

    @Test
    fun testAcceptQuestFromRoom() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD)
        }

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you accept the quest: `Find a recruiter for the Praetorian Guard`")
    }

    @Test
    fun testAcceptRequiresAQuestGiver() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        test.createPlayerMob()

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't find that quest.")
    }

    @Test
    fun testCannotAcceptAQuestTwice() {
        // setup
        val test = createTestServiceWithResetDB()
        val mob = test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
        }

        // given
        createQuestEntity(transaction { mob.mobCard!! }, QuestType.FIND_PRAETORIAN_GUARD_RECRUITER)

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("they cannot grant you that.")
    }
}
