package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.mob.Form
import kotlinmud.mob.Size
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType

class Human : Race {
    override val type: RaceType = RaceType.HUMAN
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf()
    override val vulnerableTo: List<DamageType> = listOf()
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = Attributes()
    override val size: Size = Size.MEDIUM
}
