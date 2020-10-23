package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.math.dice
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.factory.createHitAttack
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.skill.type.SkillType
import org.jetbrains.exposed.sql.transactions.transaction

class ThirdAttackObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Round) {
            transaction {
                checkThirdAttack(attacker, attackerAttacks)
                checkThirdAttack(defender, defenderAttacks)
            }
        }
    }

    private fun checkThirdAttack(mob: MobDAO, attacks: MutableList<Attack>) {
        mob.skills.firstOrNull { it.type == SkillType.THIRD_ATTACK }?.let {
            if (dice(1, 4) < it.level / 20) {
                attacks.add(createHitAttack(mob))
            }
        }
    }
}
