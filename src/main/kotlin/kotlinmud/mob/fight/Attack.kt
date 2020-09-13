package kotlinmud.mob.fight

import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.skill.type.SkillType

class Attack(
    val attackResult: AttackResult,
    val attackVerb: String,
    var damage: Int,
    val damageType: DamageType,
    val skillType: SkillType? = null
)
