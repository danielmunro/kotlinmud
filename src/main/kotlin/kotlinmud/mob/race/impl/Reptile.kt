package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.createStats
import kotlinmud.mob.Form
import kotlinmud.mob.Size
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType

class Reptile : Race {
    override val type: RaceType = RaceType.REPTILE
    override val playable: Boolean = false
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.FIRE, DamageType.SLASH)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.COLD, DamageType.PIERCE)
    override val unarmedAttackVerb: String = "bite"
    override val form: Form = Form.REPTILE
    override val attributes: Attributes = createStats(0, 0, 0, 0, 0, 0, 1)
    override val size: Size = Size.SMALL
    override val maxAppetite: Int = 4
    override val maxThirst: Int = 4
}
