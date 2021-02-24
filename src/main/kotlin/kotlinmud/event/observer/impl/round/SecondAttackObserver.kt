package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.math.dice
import kotlinmud.mob.factory.createHitAttack
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.type.SkillType
import org.jetbrains.exposed.sql.transactions.transaction

class SecondAttackObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Round) {
            transaction {
                checkSecondAttack(attacker, attackerAttacks)
                checkSecondAttack(defender, defenderAttacks)
            }
        }
    }

    private fun checkSecondAttack(mob: Mob, attacks: MutableList<Attack>) {
        mob.skills[SkillType.SECOND_ATTACK]?.let {
            if (dice(1, 5) < it / 20) {
                attacks.add(createHitAttack(mob))
            }
        }
    }
}
