package kotlinmud.mob.skill.impl.benediction

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.helper.math.dice
import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell
import kotlinmud.mob.type.Intent

class Bless : Spell {
    override val type = SkillType.BLESS
    override val levelObtained = mapOf(
        clericAt(5)
    )
    override val difficulty = mapOf(
        normalForCleric()
    )
    override val intent = Intent.PROTECTIVE

    override fun cast(caster: Mob, target: Mob) {
        target.affects.add(Affect(AffectType.BLESS, caster.level))
    }

    override fun createMessage(caster: Mob, target: Mob): Message {
        TODO("Not yet implemented")
    }
}
