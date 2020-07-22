package kotlinmud.mob.race.type

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

interface Race {
    val type: RaceType
    val playable: Boolean
    val immuneTo: List<DamageType>
    val resist: List<DamageType>
    val vulnerableTo: List<DamageType>
    val unarmedAttackVerb: String
    val unarmedDamageType: DamageType
    val form: Form
    val attributes: AttributesDAO
    val size: Size
    val maxAppetite: Int
    val maxThirst: Int
}
