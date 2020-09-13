package kotlinmud.event.observer.impl.round

import kotlin.math.roundToInt
import kotlin.random.Random
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.helper.math.dice
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.skill.type.SkillType

class EnhancedDamageObserver : Observer {
    override val eventType = EventType.FIGHT_ROUND

    override fun <T> processEvent(event: Event<T>) {
        with(event.subject as Round) {
            this.attackerAttacks.forEach { checkEnhancedDamage(this.attacker, it) }
            this.defenderAttacks.forEach { checkEnhancedDamage(this.defender, it) }
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
