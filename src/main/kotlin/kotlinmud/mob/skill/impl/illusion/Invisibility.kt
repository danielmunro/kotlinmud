package kotlinmud.mob.skill.impl.illusion

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForMage
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Intent

class Invisibility : Spell {
    override val type: SkillType = SkillType.INVISIBILITY
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        mageAt(5),
        clericAt(35)
    )
    override val difficulty = mapOf(
        easyForMage(),
        normalForCleric()
    )
    override val intent = Intent.PROTECTIVE

    override fun cast(caster: Mob, target: Mob) {
        target.affects.add(Affect(AffectType.INVISIBILITY, caster.level))
    }

    override fun createMessage(caster: Mob, target: Mob): Message {
        val toObservers = "$target fades out of existence."
        val toSelf = "you fade out of existence."

        return MessageBuilder()
            .toActionCreator(if (caster == target) toSelf else toObservers)
            .toTarget(toSelf)
            .toObservers(toObservers)
            .build()
    }
}
