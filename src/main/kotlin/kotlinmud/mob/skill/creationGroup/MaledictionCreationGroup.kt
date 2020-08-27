package kotlinmud.mob.skill.creationGroup

import kotlinmud.mob.skill.impl.malediction.Blind
import kotlinmud.mob.skill.impl.malediction.Curse
import kotlinmud.mob.skill.type.CreationGroup

class MaledictionCreationGroup : CreationGroup {
    override val name = "malediction"
    override val skills = listOf(
        Blind(),
        Curse()
    )
    override val points: Int = 8
}
