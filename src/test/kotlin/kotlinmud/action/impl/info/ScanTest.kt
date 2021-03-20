package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import kotlinmud.room.type.Area
import kotlinmud.test.helper.createTestService
import org.junit.Test

class ScanTest {
    @Test
    fun testCanScan() {
        // setup
        val test = createTestService()
        val room = test.createRoomBuilder().also {
            it.area = Area.None
        }.build()

        val mob1 = test.createPlayerMob()
        val mob2 = test.createPlayerMob()
        val mob3 = test.createPlayerMob()

        // given
        mob3.room = room

        // when
        val response = test.runAction("scan")

        // then
        assertThat(response.message.toActionCreator).contains(mob2.name)
        assertThat(response.message.toActionCreator).doesNotContain(mob3.name)
    }
}
