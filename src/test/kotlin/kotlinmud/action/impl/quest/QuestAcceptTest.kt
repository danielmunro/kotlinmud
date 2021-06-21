package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.quest.model.QuestProgress
import kotlinmud.quest.type.QuestType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import kotlinmud.type.RoomCanonicalId
import org.junit.Test

class QuestAcceptTest {
    @Test
    fun testCanAcceptAQuest() {
        // setup
        val test = createTestService()
        val quest = test.findQuest(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER)!!

        // given
        val mob = test.createPlayerMob {
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.FindRecruiterPraetorianGuard }!!
        }
        val count = mob.quests.size

        // when
        val response = test.runAction("quest accept ${getIdentifyingWord(quest)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you accept the quest: `${quest.name}`")
        assertThat(mob.quests).hasSize(count + 1)
    }

    @Test
    fun testAcceptQuestFromRoom() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMob {
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.FindRecruiterPraetorianGuard }!!
        }

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you accept the quest: `find a recruiter for the Praetorian Guard`")
    }

    @Test
    fun testAcceptRequiresAQuestGiver() {
        // setup
        val test = createTestService()

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
        val test = createTestService()
        val mob = test.createPlayerMob {
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.FindRecruiterPraetorianGuard }!!
        }

        // given
        mob.quests[QuestType.FIND_PRAETORIAN_GUARD_RECRUITER] = QuestProgress()

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't find that quest.")
    }

    @Test
    fun testMustSatisfyAllConditions() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMob {
            it.room = test.getStartRoom()
        }

        // when
        val response = test.runAction("quest accept recruiter")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you can't find that quest.")
    }
}
