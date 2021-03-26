package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.hardForThief
import kotlinmud.mob.skill.factory.normalForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.veryHardForCleric
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Bash : Skill, Customization {
    override val type = SkillType.BASH
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "bash"
    override val points = 6
    override val levelObtained = mapOf(
        warriorAt(1),
        thiefAt(30),
        clericAt(45)
    )
    override val difficulty = mapOf(
        normalForWarrior(),
        hardForThief(),
        veryHardForCleric()
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.INPUT
    override val helpText = "tbd"
}
