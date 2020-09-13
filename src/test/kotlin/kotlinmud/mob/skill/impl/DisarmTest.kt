package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlinmud.io.type.IOStatus
import kotlinmud.item.type.Position
import kotlinmud.mob.skill.factory.createSkill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class DisarmTest {
    @Test
    fun testDisarmSanity() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMob {
            createSkill(SkillType.DISARM, it, 100)
        }
        val target = test.createMob()
        val initialSize = transaction { test.getStartRoom().items.toList() }.size

        // when
        val response = test.runActionForIOStatus(mob, "disarm ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You disarm $target and send their weapon flying!")
        assertThat(response.message.toTarget).isEqualTo("$mob disarms you and sends your weapon flying!")
        assertThat(response.message.toObservers).isEqualTo("$mob disarms $target and sends their weapon flying!")

        // and
        assertThat(target.getEquippedByPosition(Position.WEAPON)).isNull()
        assertThat(transaction { test.getStartRoom().items.toList() }).hasSize(initialSize + 1)
    }

    @Test
    fun testDisarmRequiresWeapon() {
        // setup
        val test = createTestService()

        // given
        test.createMob {
            createSkill(SkillType.DISARM, it, 100)
        }
        val target = test.createMob()
        transaction {
            target.getEquippedByPosition(Position.WEAPON)!!.let {
                it.mobEquipped = null
                it.mobInventory = null
            }
        }

        // when
        val response = test.runAction("disarm ${getIdentifyingWord(target)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("They are not equipped with a weapon.")
    }
}
