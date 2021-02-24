package kotlinmud.mob.factory

import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.type.SkillType

fun createHitAttack(mob: Mob): Attack {
    return Attack(
        AttackResult.HIT,
        mob.getAttackVerb(),
        mob.calculateDamage(),
        mob.getDamageType()
    )
}

fun createMissAttack(mob: Mob): Attack {
    return Attack(
        AttackResult.MISS,
        mob.getAttackVerb(),
        0,
        mob.getDamageType()
    )
}

fun createEvadeAttack(mob: Mob, skillType: SkillType): Attack {
    return Attack(
        AttackResult.EVADE,
        mob.getAttackVerb(),
        0,
        mob.getDamageType(),
        skillType
    )
}
