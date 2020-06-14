package kotlinmud.mob.race.impl

import kotlinmud.attributes.model.Attributes
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Avian : Race {
    override val type: RaceType = RaceType.AVIAN
    override val playable: Boolean = false
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf()
    override val vulnerableTo: List<DamageType> = listOf()
    override val unarmedAttackVerb: String = "peck"
    override val unarmedDamageType: DamageType = DamageType.PIERCE
    override val form: Form = Form.BIRD
    override val attributes: Attributes = Attributes()
    override val size: Size = Size.TINY
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 3
}
