package kotlinmud.mob.skill.creationGroup.spell

import kotlinmud.mob.skill.impl.benediction.Bless
import kotlinmud.mob.skill.impl.curative.CurePoison
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SpellGroup

class CurativeSpellGroup : SpellGroup, Customization {
    override val name = "curative"
    override val spells = listOf(
        CurePoison()
    )
    override val points: Int = 8
    override val creationGroupType = CreationGroupType.SPELL_GROUP
    override val helpText = "tbd"
}