package kotlinmud.mob.skill.creationGroup

import kotlinmud.mob.skill.creationGroup.spell.MaledictionSpellGroup
import kotlinmud.mob.skill.impl.weapon.Wand
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.CustomizationGroup

class MageDefaultCreationGroup : Customization, CustomizationGroup {
    override val name = "mage"
    override val customizations = listOf(
        MaledictionSpellGroup(),
        Wand()
    )
    override val points = 8
    override val creationGroupType = CreationGroupType.DEFAULT_GROUP
}
