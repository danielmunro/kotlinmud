package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.race.impl.Bear
import kotlinmud.mob.repository.findMobById
import kotlinmud.mob.skill.factory.createSkill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class BackstabTest {
    @Test
    fun testCanBackstab() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob {
            createSkill(SkillType.BACKSTAB, it, 100)
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(mob, "backstab ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You stab $target in the back.")
        assertThat(response.message.toTarget).isEqualTo("$mob stabs you in the back.")
        assertThat(response.message.toObservers).isEqualTo("$mob stabs $target in the back.")

        // and
        assertThat(findMobById(target.id.value).hp).isLessThan(target.calc(Attribute.HP))
    }

    @Test
    fun testBackstabFailsOnSaveAgainstPierce() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob {
            createSkill(SkillType.BACKSTAB, it, 1)
        }
        val target = test.createMob {
            it.race = Bear()
        }

        // when
        val response = test.runActionForIOStatus(
            mob,
            "backstab ${getIdentifyingWord(target)}",
            IOStatus.FAILED) {
                transaction {
                    findMobById(mob.id.value).mv = 100
                }
            }

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You lost your concentration.")
    }
}
