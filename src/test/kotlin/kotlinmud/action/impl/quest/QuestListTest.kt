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
        test.createPlayerMob {
            it.room = findRoomByCanonicalId(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
        }

        // when
        val response = test.runAction("quest list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Talk to Captain Bartok of the Praetorian Guard\n")
    }
}