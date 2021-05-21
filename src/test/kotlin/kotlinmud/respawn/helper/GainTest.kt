package kotlinmud.respawn.helper

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlinmud.mob.race.impl.Faerie
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.test.helper.createTestService
import org.junit.Test

class GainTest {
    @Test
    fun testRacialGainsTest() {
        // setup
        val test = createTestService()

        // given
        val ogre = test.createMobBuilder().also {
            it.level = 20
            it.race = Ogre()
        }

        val faerie = test.createMobBuilder().also {
            it.level = 20
            it.race = Faerie()
        }

        // when
        val gain1 = calculateHpForMob(
            ogre.level,
            ogre.race,
        )

        val gain2 = calculateHpForMob(
            faerie.level,
            faerie.race,
        )

        // then
        assertThat(gain1).isGreaterThan(gain2)
    }
}
