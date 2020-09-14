package kotlinmud.mob.factory

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.skill.type.SkillType

fun createHitAttack(mob: MobDAO): Attack {
    return Attack(
        AttackResult.HIT,
        mob.getAttackVerb(),
        mob.calculateDamage(),
        mob.getDamageType()
    )
}

fun createMissAttack(mob: MobDAO): Attack {
    return Attack(
        AttackResult.MISS,
        mob.getAttackVerb(),
        0,
        mob.getDamageType()
    )
}

fun createEvadeAttack(mob: MobDAO, skillType: SkillType): Attack {
    return Attack(
        AttackResult.EVADE,
        mob.getAttackVerb(),
        0,
        mob.getDamageType(),
        skillType
    )
}
