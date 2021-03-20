package kotlinmud.action.impl.item

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class QuaffTest {
    @Test
    fun testCanQuaffPotion() {
        // setup
        val test = createTestService()

        // given
        val potion = test.createPotion()
        test.createPlayerMob {
            it.items.add(potion)
        }

        // when
        val response = test.runAction("quaff ${getIdentifyingWord(potion)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you quaff $potion")
    }

    @Test
    fun testQuaffConsumesPotion() {
        // setup
        val test = createTestService()
        val potion = test.createPotion()

        // given
        val mob = test.createPlayerMob {
            it.items.add(potion)
        }
        val count = mob.items.size

        // when
        test.runAction("quaff ${getIdentifyingWord(potion)}")

        // then
        assertThat(mob.items).hasSize(count - 1)
    }

    @Test
    fun testQuaffImpartsAffects() {
        // setup
        val test = createTestService()
        val potion = test.createPotion()

        // given
        potion.affects.add(Affect(AffectType.BLESS, 1))
        val mob = test.createPlayerMob {
            it.items.add(potion)
        }
        val count = mob.affects.size

        // when
        test.runAction("quaff ${getIdentifyingWord(potion)}")

        // then
        assertThat(mob.affects).hasSize(count + 1)
    }
}
