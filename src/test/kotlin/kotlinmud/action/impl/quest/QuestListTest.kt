package kotlinmud.action.impl.quest

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class QuestListTest {
    @Test
    fun testCanListQuestsFromARoom1() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMob {
            it.room = test.findRoom { room -> room.id == 204 }!!
        }

        // when
        val response = test.runAction("quest list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("collect mushrooms for Barbosa\n")
    }

    @Test
    fun testCanListQuestsFromARoom2() {
        // setup
        val test = createTestService()

        // given
        test.createPlayerMob {
            it.room = test.findRoom { room -> room.id == 201 }!!
        }

        // when
        val response = test.runAction("quest list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("join the praetorian guard\n")
    }
}
