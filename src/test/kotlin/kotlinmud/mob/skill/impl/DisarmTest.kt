package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Position
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class DisarmTest {
    @Test
    fun testDisarmSanity() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMob {
            it.skills[SkillType.DISARM] = 100
        }
        val target = test.createMob()
        val initialSize = test.getStartRoom().items.size

        // when
        val response = test.runActionForIOStatus(mob, "disarm ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You disarm $target and send their weapon flying!")
        assertThat(response.message.toTarget).isEqualTo("$mob disarms you and sends your weapon flying!")
        assertThat(response.message.toObservers).isEqualTo("$mob disarms $target and sends their weapon flying!")

        // and
        assertThat(target.getEquippedByPosition(Position.WEAPON)).isNull()
        assertThat(test.getStartRoom().items).hasSize(initialSize + 1)
    }

    @Test
    fun testDisarmRequiresWeapon() {
        // setup
        val test = createTestService()

        // given
        test.createMob {
            it.skills[SkillType.DISARM] = 100
        }
        val target = test.createMob()
        target.equipped.clear()

        // when
        val response = test.runAction("disarm ${getIdentifyingWord(target)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("They are not equipped with a weapon.")
    }
}
