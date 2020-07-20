package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

data class Dwarf(override val type: RaceType = RaceType.DWARF) : Race {
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.POUND)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.DROWNING)
    override val unarmedAttackVerb: String = "punch"
    override val unarmedDamageType: DamageType = DamageType.POUND
    override val form: Form = Form.MAMMAL
    override val attributes: AttributesDAO =
        createStats(2, -2, 0, -1, 1, 0, 1)
    override val size: Size = Size.SMALL
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 3
}
