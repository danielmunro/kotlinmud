package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.helper.math.dice
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.factory.createHitAttack
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.skill.type.SkillType

fun thirdAttackEvent(event: Event<*>) {
    with(event.subject as Round) {
        checkThirdAttack(this.attacker, this.attackerAttacks)
        checkThirdAttack(this.defender, this.defenderAttacks)
    }
}

private fun checkThirdAttack(mob: MobDAO, attacks: MutableList<Attack>) {
    mob.skills.firstOrNull { it.type == SkillType.THIRD_ATTACK }?.let {
        if (dice(1, 4) < it.level / 20) {
            attacks.add(createHitAttack(mob))
        }
    }
}
