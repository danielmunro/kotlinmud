package kotlinmud.mob.race.type

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.emptyAttributes
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

abstract class Race {
    abstract val type: RaceType
    open val immuneTo = listOf<DamageType>()
    open val resist = listOf<DamageType>()
    open val vulnerableTo = listOf<DamageType>()
    abstract val unarmedAttackVerb: String
    abstract val unarmedDamageType: DamageType
    abstract val form: Form
    open val attributes = emptyAttributes()
    abstract val size: Size
    abstract val maxAppetite: Int
    abstract val maxThirst: Int
}
