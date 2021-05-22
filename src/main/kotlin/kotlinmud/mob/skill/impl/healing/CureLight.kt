package kotlinmud.mob.skill.impl.healing

import kotlinmud.helper.math.dice
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell
import kotlinmud.mob.type.Intent

class CureLight : Spell {
    override val type = SkillType.CURE_LIGHT
    override val levelObtained = mapOf(clericAt(1))
    override val difficulty = mapOf(easyForCleric())
    override val intent = Intent.PROTECTIVE

    override fun cast(caster: Mob, target: Mob) {
        val amount = dice(1, 6)
        target.increaseHp(amount)
    }

    override fun createMessage(caster: Mob, target: Mob): Message {
        val toObservers = "$target feels better!"
        val toSelf = "you feel better!"

        return MessageBuilder()
            .toActionCreator(if (caster == target) toSelf else toObservers)
            .toTarget(toSelf)
            .toObservers(toObservers)
            .build()
    }
}
