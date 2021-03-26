package kotlinmud.mob.skill.impl.malediction

import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell
import kotlinmud.mob.type.Intent

class Blind : Spell {
    override val type = SkillType.BLIND
    override val levelObtained = mapOf(
        mageAt(5)
    )
    override val difficulty = mapOf(
        easyForCleric()
    )
    override val intent = Intent.OFFENSIVE

    override fun createMessage(caster: Mob, target: Mob): Message {
        TODO("Not yet implemented")
    }
}
