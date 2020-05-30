package kotlinmud.mob.race.impl

import kotlinmud.attributes.createStats
import kotlinmud.attributes.model.Attributes
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Faerie : Race {
    override val type: RaceType = RaceType.FAERIE
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.MENTAL, DamageType.ENERGY)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.POUND)
    override val unarmedAttackVerb: String = "slap"
    override val unarmedDamageType: DamageType = DamageType.POUND
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(-3, 2, 2, 2, -3, 1, -1)
    override val size: Size = Size.TINY
    override val maxAppetite: Int = 2
    override val maxThirst: Int = 2
}
