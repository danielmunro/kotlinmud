package kotlinmud.mob.skill.impl.enhancement

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
import kotlinmud.mob.type.Intent

class GiantStrength : Spell {
    override val type = SkillType.GIANT_STRENGTH
    override val levelObtained = mapOf(
        mageAt(15),
        clericAt(25)
    )
    override val difficulty = mapOf(
        easyForMage(),
        normalForCleric()
    )
    override val intent = Intent.PROTECTIVE

    override fun cast(caster: Mob, target: Mob) {
        target.affects.add(Affect(AffectType.GIANT_STRENGTH, caster.level))
    }

    override fun createMessage(caster: Mob, target: Mob): Message {
        val label = if (caster == target) "your" else "${target.name}'s"
        return MessageBuilder()
            .toActionCreator("$label muscles surge with heightened power.")
            .toTarget("your muscles surge with heightened power.")
            .toObservers("$target's muscles surge with heightened power.")
            .build()
    }
}
