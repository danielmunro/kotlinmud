package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.createStats
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Giant : Race {
    override val type: RaceType = RaceType.GIANT
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.FIRE, DamageType.COLD)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.ENERGY, DamageType.MENTAL)
    override val unarmedAttackVerb: String = "punch"
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(3, -2, -2, -2, 3, 0, 1)
    override val size: Size = Size.HUGE
    override val maxAppetite: Int = 5
    override val maxThirst: Int = 4
}
