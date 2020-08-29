package kotlinmud.mob.skill.creationGroup

import kotlinmud.mob.skill.impl.Dodge
import kotlinmud.mob.skill.impl.Parry
import kotlinmud.mob.skill.impl.weapon.Dagger
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.CustomizationGroup

class ThiefDefaultCreationGroup : Customization, CustomizationGroup {
    override val name = "thief default"
    override val customizations = listOf<Customization>(
        Dodge(),
        Parry(),
        Dagger()
    )
    override val points = 8
    override val creationGroupType = CreationGroupType.DEFAULT_GROUP
}
