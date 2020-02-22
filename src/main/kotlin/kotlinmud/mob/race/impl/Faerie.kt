package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.createStats
import kotlinmud.mob.Form
import kotlinmud.mob.Size
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType

class Faerie : Race {
    override val type: RaceType = RaceType.FAERIE
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.MENTAL, DamageType.ENERGY)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.POUND)
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(-3, 2, 2, 2, -3, 1, -1)
    override val size: Size = Size.TINY
}
