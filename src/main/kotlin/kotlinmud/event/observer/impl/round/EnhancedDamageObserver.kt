package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.math.dice
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.type.SkillType
import kotlin.math.roundToInt
import kotlin.random.Random

class EnhancedDamageObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Round) {
            attackerAttacks.forEach { checkEnhancedDamage(attacker, it) }
            defenderAttacks.forEach { checkEnhancedDamage(defender, it) }
        }
    }

    private fun checkEnhancedDamage(mob: Mob, attack: Attack) {
        mob.skills[SkillType.ENHANCED_DAMAGE]?.let {
            if (dice(1, 5) < it / 20) {
                val amount = attack.damage * Random.nextDouble(0.03)
                attack.damage += amount.roundToInt()
            }
        }
    }
}
