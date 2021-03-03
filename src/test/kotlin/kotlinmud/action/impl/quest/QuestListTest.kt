package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import kotlinmud.type.RoomCanonicalId
import org.junit.Test

class QuestListTest {
    @Test
    fun testCanListQuestsFromAMobQuestGiver() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMob {
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD }!!
        }

        // when
        val response = test.runAction("quest list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Find a recruiter for the Praetorian Guard\n")
    }

    @Test
    fun testCanListQuestsFromARoom() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMob {
            it.room = test.findRoom { room -> room.canonicalId == RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD }!!
        }

        // when
        val response = test.runAction("quest list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Find a recruiter for the Praetorian Guard\n")
    }
}
