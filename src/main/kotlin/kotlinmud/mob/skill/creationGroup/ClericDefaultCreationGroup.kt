package kotlinmud.mob.skill.creationGroup

import kotlinmud.mob.skill.creationGroup.spell.HealingSpellGroup
import kotlinmud.mob.skill.impl.weapon.Mace
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.CustomizationGroup

class ClericDefaultCreationGroup : Customization, CustomizationGroup {
    override val name = "cleric default"
    override val customizations = listOf(
        HealingSpellGroup(),
        Mace()
    )
    override val points = 6
    override val creationGroupType = CreationGroupType.DEFAULT_GROUP
}
