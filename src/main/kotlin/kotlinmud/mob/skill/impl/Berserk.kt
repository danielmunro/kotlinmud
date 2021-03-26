package kotlinmud.mob.skill.impl

import kotlinmud.mob.skill.factory.hardForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Berserk : Skill, Customization {
    override val type = SkillType.BERSERK
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "berserk"
    override val points = 8
    override val levelObtained = mapOf(
        warriorAt(1),
        thiefAt(45)
    )
    override val difficulty = mapOf(
        hardForWarrior()
    )
    override val intent = Intent.NEUTRAL
    override val invokesOn = SkillInvokesOn.INPUT
    override val helpText = "tbd"
}
