package kotlinmud.mob.fight

import kotlinmud.mob.skill.SkillType

class Attack(
    val attackResult: AttackResult,
    val damage: Int,
    val damageType: DamageType,
    val skillType: SkillType? = null
)
