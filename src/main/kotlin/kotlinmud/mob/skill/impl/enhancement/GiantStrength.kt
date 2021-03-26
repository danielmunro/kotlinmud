package kotlinmud.mob.skill.impl.enhancement

import kotlinmud.io.model.Message
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

    override fun createMessage(caster: Mob, target: Mob): Message {
        TODO("Not yet implemented")
    }
}
