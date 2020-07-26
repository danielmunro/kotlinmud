package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.emptyAttributes
import kotlinmud.mob.fight.type.DamageType
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
    override val attributes: AttributesDAO = emptyAttributes()
    override val size: Size = Size.TINY
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 3
}
