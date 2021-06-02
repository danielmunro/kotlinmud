package kotlinmud.mob.skill.impl.enhancement

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.easyForMage
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell
import kotlinmud.mob.type.Intent

class Haste : Spell {
    override val type = SkillType.HASTE
    override val levelObtained = mapOf(
        mageAt(15),
    )
    override val difficulty = mapOf(
        easyForMage(),
    )
    override val intent = Intent.PROTECTIVE
    override fun cast(caster: Mob, target: Mob) {
        target.affects.add(Affect(AffectType.HASTE, caster.level))
    }

    override fun createMessage(caster: Mob, target: Mob): Message {
        val label = if (caster == target) "you" else target.name
        return MessageBuilder()
            .toActionCreator("$label start moving quickly.")
            .toTarget("you start moving quickly.")
            .toObservers("$target starts moving quickly.")
            .build()
    }
}
