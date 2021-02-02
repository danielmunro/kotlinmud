package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.test.createTestService
import kotlinmud.type.CanonicalId
import org.junit.Test

class QuestListTest {
    @Test
    fun testCanListQuests() {
        // setup
        val test = createTestService()

        // given
        test.createMob {
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_RECRUITER_1)
        }

        // when
        val response = test.runAction("quest list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Find Recruiter Bartok for the Praetorian Guard\n")
    }
}
