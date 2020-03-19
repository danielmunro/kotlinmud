package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.createStats
import kotlinmud.mob.Form
import kotlinmud.mob.Size
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType

class Elf : Race {
    override val type: RaceType = RaceType.ELF
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.MENTAL)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.FIRE, DamageType.COLD)
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(-2, 1, 2, 1, -2)
    override val size: Size = Size.SMALL
    override val maxAppetite: Int = 2
    override val maxThirst: Int = 2
}
