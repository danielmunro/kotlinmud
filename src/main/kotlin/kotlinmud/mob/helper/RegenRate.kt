package kotlinmud.mob.helper

import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.math.dice
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.room.type.RegenLevel

fun getRoomRegenRate(regenLevel: RegenLevel): Double {
    return when (regenLevel) {
        RegenLevel.NONE -> 0.0
        RegenLevel.LOW -> 0.05
        RegenLevel.NORMAL -> 0.1
        RegenLevel.HIGH -> .20
        RegenLevel.FULL -> 1.0
    }
}

fun getDispositionRegenRate(disposition: Disposition): Double {
    return when (disposition) {
        Disposition.DEAD -> 0.0
        Disposition.SLEEPING -> 0.15
        Disposition.SITTING -> 0.05
        Disposition.STANDING -> 0.0
        Disposition.FIGHTING -> -0.15
    }
}

fun getSkillBoostRegenRate(mob: Mob, attribute: Attribute): Double {
    return when (attribute) {
        Attribute.HP -> mob.getSkill(SkillType.FAST_HEALING)?.let { if (rollRegen(it.level)) 0.1 else 0.0 } ?: 0.0
        Attribute.MANA -> mob.getSkill(SkillType.MEDITATION)?.let { if (rollRegen(it.level)) 0.1 else 0.0 } ?: 0.0
        else -> 0.0
    }
}

fun rollRegen(skillLevel: Int): Boolean {
    return dice(1, skillLevel) > 50
}
