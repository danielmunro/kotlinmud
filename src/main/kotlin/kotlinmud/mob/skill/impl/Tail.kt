package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.easyForWarrior
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Tail : Skill {
    override val type = SkillType.TAIL
    override val levelObtained = mapOf(
        warriorAt(1)
    )
    override val difficulty = mapOf(
        easyForWarrior()
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.INPUT
}
