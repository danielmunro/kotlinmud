package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.createStats
import kotlinmud.mob.Form
import kotlinmud.mob.Size
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType

class Kender : Race {
    override val type: RaceType = RaceType.KENDER
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf()
    override val vulnerableTo: List<DamageType> = listOf(DamageType.ENERGY)
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(-1, 0, 1, 2, 1, 1, 0)
    override val size: Size = Size.MEDIUM
    override val maxAppetite: Int = 2
    override val maxThirst: Int = 3
}
