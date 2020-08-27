package kotlinmud.mob.skill.helper

import kotlinmud.mob.skill.creationGroup.HealingCreationGroup
import kotlinmud.mob.skill.creationGroup.IllusionCreationGroup
import kotlinmud.mob.skill.creationGroup.MaledictionCreationGroup
import kotlinmud.mob.skill.type.CreationGroup

fun createCreationGroupList(): List<CreationGroup> {
    return listOf(
        HealingCreationGroup(),
        IllusionCreationGroup(),
        MaledictionCreationGroup()
    )
}
