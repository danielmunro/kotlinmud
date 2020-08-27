package kotlinmud.mob.skill.creationGroup

import kotlinmud.mob.skill.impl.illusion.Invisibility
import kotlinmud.mob.skill.type.CreationGroup

class IllusionCreationGroup : CreationGroup {
    override val name = "illusion"
    override val skills = listOf(
        Invisibility()
    )
    override val points = 8
}
