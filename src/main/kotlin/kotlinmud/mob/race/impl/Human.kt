package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Human : Race {
    override val type: RaceType = RaceType.HUMAN
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf()
    override val vulnerableTo: List<DamageType> = listOf()
    override val unarmedAttackVerb: String = "punch"
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = Attributes()
    override val size: Size = Size.MEDIUM
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 3
}
