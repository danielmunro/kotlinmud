package kotlinmud.mob.race.type

import kotlinmud.attributes.type.Attribute
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
    open val attributes: MutableMap<Attribute, Int> = mutableMapOf()
    abstract val size: Size
    abstract val maxAppetite: Int
    abstract val maxThirst: Int

    override fun equals(other: Any?): Boolean {
        return if (other is Race) other.type == type else super.equals(other)
    }
}
