package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

data class Giant(override val type: RaceType = RaceType.GIANT) : Race {
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.FIRE, DamageType.COLD)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.ENERGY, DamageType.MENTAL)
    override val unarmedAttackVerb: String = "punch"
    override val unarmedDamageType: DamageType = DamageType.POUND
    override val form: Form = Form.MAMMAL
    override val attributes: AttributesDAO = createStats(3, -2, -2, -2, 3, 0, 1)
    override val size: Size = Size.HUGE
    override val maxAppetite: Int = 5
    override val maxThirst: Int = 4
}
