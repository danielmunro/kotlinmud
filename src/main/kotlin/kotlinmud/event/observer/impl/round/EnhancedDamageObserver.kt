package kotlinmud.event.observer.impl.round

import kotlin.math.roundToInt
import kotlin.random.Random
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.math.dice
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.skill.type.SkillType
import org.jetbrains.exposed.sql.transactions.transaction

class EnhancedDamageObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Round) {
            transaction {
                attackerAttacks.forEach { checkEnhancedDamage(attacker, it) }
                defenderAttacks.forEach { checkEnhancedDamage(defender, it) }
            }
        }
    }

    private fun checkEnhancedDamage(mob: MobDAO, attack: Attack) {
        mob.skills.firstOrNull { it.type == SkillType.ENHANCED_DAMAGE }?.let {
            if (dice(1, 5) < it.level / 20) {
                val amount = attack.damage * Random.nextDouble(0.03)
                attack.damage += amount.roundToInt()
            }
        }
    }
}
