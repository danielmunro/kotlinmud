package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.hardForThief
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Backstab : Skill {
    override val type = SkillType.BACKSTAB
    override val levelObtained = mapOf(
        thiefAt(1)
    )
    override val difficulty = mapOf(
        hardForThief()
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.INPUT
}
