package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.emptyAttributes
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Lizard : Race {
    override val type: RaceType = RaceType.LIZARD
    override val playable: Boolean = false
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.FIRE)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.COLD)
    override val unarmedAttackVerb: String = "bite"
    override val unarmedDamageType: DamageType = DamageType.SLASH
    override val form: Form = Form.REPTILE
    override val attributes: AttributesDAO = emptyAttributes()
    override val size: Size = Size.TINY
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 3
}
