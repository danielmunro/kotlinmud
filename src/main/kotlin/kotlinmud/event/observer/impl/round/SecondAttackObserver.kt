package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.helper.math.dice
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.factory.createHitAttack
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.skill.type.SkillType

fun secondAttackEvent(event: Event<*>) {
    with(event.subject as Round) {
        checkSecondAttack(this.attacker, this.attackerAttacks)
        checkSecondAttack(this.defender, this.defenderAttacks)
    }
}

private fun checkSecondAttack(mob: MobDAO, attacks: MutableList<Attack>) {
    mob.skills.firstOrNull { it.type == SkillType.SECOND_ATTACK }?.let {
        if (dice(1, 5) < it.level / 20) {
            attacks.add(createHitAttack(mob))
        }
    }
}
