package kotlinmud.player

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.tylerthrailkill.helpers.prettyprint.pp
import java.lang.StringBuilder
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.player.loader.MobCardLoader
import kotlinmud.player.mapper.mapMobCard
import kotlinmud.test.createTestService
import org.junit.Test

class MobCardTest {
    @Test
    fun testMobCardCanMapAndLoad() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMobBuilder().build()
        val mobCard = test.findMobCardByName(mob.name)!!

        // given
        val data = mapMobCard(mobCard)

        // when
        val model = MobCardLoader(Tokenizer(data)).load()

        // then
        val buf1 = StringBuilder()
        pp(mobCard, 2, buf1, 80)
        val buf2 = StringBuilder()
        pp(model, 2, buf2, 80)
        assertThat(buf2.toString()).isEqualTo(buf1.toString())
    }
}
