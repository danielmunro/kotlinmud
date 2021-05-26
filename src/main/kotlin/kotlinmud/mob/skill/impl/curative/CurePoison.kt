package kotlinmud.mob.skill.impl.curative

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell
import kotlinmud.mob.type.Intent

class CurePoison : Spell {
    override val type = SkillType.CURE_POISON
    override val levelObtained = mapOf(
        clericAt(10)
    )
    override val difficulty = mapOf(
        easyForCleric(),
    )
    override val intent = Intent.PROTECTIVE
    override fun cast(caster: Mob, target: Mob) {
        target.affects.removeIf {
            it.type == AffectType.POISON
        }
    }

    override fun createMessage(caster: Mob, target: Mob): Message {
        val label = if (caster == target) "you" else target.name
        return MessageBuilder()
            .toActionCreator("$label feel less ill.")
            .toTarget("you feel less ill.")
            .toObservers("$target feels less ill.")
            .build()
    }
}