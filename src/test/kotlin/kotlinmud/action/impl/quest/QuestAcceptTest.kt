package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.test.createTestService
import kotlinmud.type.CanonicalId
import org.junit.Test

class QuestAcceptTest {
    @Test
    fun testCanAcceptAQuest() {
        // setup
        val test = createTestService()

        test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_RECRUITER_1)
        }

        val response = test.runAction("quest accept recruiter")

        assertThat(response.message.toActionCreator).isEqualTo("you accept the quest: `Find Recruiter Bartok for the Praetorian Guard`")
    }
}
