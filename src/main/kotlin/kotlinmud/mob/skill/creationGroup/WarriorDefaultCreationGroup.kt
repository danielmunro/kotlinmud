package kotlinmud.mob.skill.creationGroup

import kotlinmud.mob.skill.impl.Bash
import kotlinmud.mob.skill.impl.Berserk
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.CustomizationGroup

class WarriorDefaultCreationGroup : CustomizationGroup, Customization {
    override val name = "warrior default"
    override val customizations = listOf<Customization>(
        Berserk(),
        Bash()
    )
    override val points = 12
    override val creationGroupType = CreationGroupType.DEFAULT_GROUP
}
