package kotlinmud.mob.skill.creationGroup.spell

import kotlinmud.mob.skill.impl.malediction.Blind
import kotlinmud.mob.skill.impl.malediction.Curse
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SpellGroup

class MaledictionSpellGroup : SpellGroup, Customization {
    override val name = "malediction"
    override val spells = listOf(
        Blind(),
        Curse()
    )
    override val points: Int = 8
    override val creationGroupType = CreationGroupType.SPELL_GROUP
}
