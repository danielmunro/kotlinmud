package kotlinmud.mob.skill.impl.healing

import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell
import kotlinmud.mob.type.Intent

class Heal : Spell {
    override val type = SkillType.HEAL
    override val levelObtained = mapOf(clericAt(30))
    override val difficulty = mapOf(easyForCleric())
    override val intent = Intent.PROTECTIVE

    override fun cast(caster: Mob, target: Mob) {
        val amount = 50
        target.increaseHp(amount)
    }

    override fun createMessage(caster: Mob, target: Mob): Message {
        TODO("Not yet implemented")
    }
}
