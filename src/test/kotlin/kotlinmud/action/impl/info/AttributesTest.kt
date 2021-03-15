package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.race.impl.Faerie
import kotlinmud.test.helper.createTestService
import org.junit.Test

class AttributesTest {
    @Test
    fun testAttributesShowBasicMob() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob()

        // when
        val response = test.runAction(mob, "attr")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Your attributes are:\nStr: 15/15 Int: 15/15 Wis: 15/15 Dex: 15/15 Con: 15/15")
    }

    @Test
    fun testAttributesShowForMobWithAffectModifiers() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMobBuilder().also {
            it.affects.add(Affect(AffectType.BLESS, 1, mapOf(Pair(Attribute.STR, 1))))
        }.build()

        // when
        val response = test.runAction(mob, "attr")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Your attributes are:\nStr: 15/16 Int: 15/15 Wis: 15/15 Dex: 15/15 Con: 15/15")
    }

    @Test
    fun testAttributesShowForMobWithRaceModifiers() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMobBuilder().also {
            it.race = Faerie()
        }.build()

        // when
        val response = test.runAction(mob, "attr")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Your attributes are:\nStr: 12/12 Int: 17/17 Wis: 17/17 Dex: 17/17 Con: 12/12")
    }

    @Test
    fun testAttributesShowForMobWithTrainModifiers() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMobBuilder().also {
            it.attributes[Attribute.WIS] = 1
        }.build()

        // when
        val response = test.runAction(mob, "attr")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Your attributes are:\nStr: 15/15 Int: 15/15 Wis: 16/16 Dex: 15/15 Con: 15/15")
    }
}
