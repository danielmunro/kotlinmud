package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Bear : Race {
    override val type: RaceType = RaceType.BEAR
    override val playable: Boolean = false
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(
        DamageType.POUND,
        DamageType.SLASH,
        DamageType.PIERCE
    )
    override val vulnerableTo: List<DamageType> = listOf()
    override val unarmedAttackVerb: String = "swipe"
    override val unarmedDamageType: DamageType = DamageType.SLASH
    override val form: Form = Form.MAMMAL
    override val attributes: AttributesDAO = createStats(2, -2, -2, 0, 2)
    override val size: Size = Size.LARGE
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 3
}
