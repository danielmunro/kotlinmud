package kotlinmud.mob.skill.creationGroup.spell

import kotlinmud.mob.skill.impl.benediction.Bless
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SpellGroup

class BenedictionSpellGroup : SpellGroup, Customization {
    override val name = "benediction"
    override val spells = listOf(
        Bless()
    )
    override val points: Int = 8
    override val creationGroupType = CreationGroupType.SPELL_GROUP
    override val helpText = "tbd"
}
