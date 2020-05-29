package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.createStats
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Dwarf : Race {
    override val type: RaceType = RaceType.DWARF
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.POUND)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.DROWNING)
    override val unarmedAttackVerb: String = "punch"
    override val unarmedDamageType: DamageType = DamageType.POUND
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(2, -2, 0, -1, 1, 0, 1)
    override val size: Size = Size.SMALL
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 3
}
