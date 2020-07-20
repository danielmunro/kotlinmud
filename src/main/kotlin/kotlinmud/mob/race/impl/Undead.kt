package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Undead : Race {
    override val type: RaceType = RaceType.UNDEAD
    override val playable: Boolean = false
    override val immuneTo: List<DamageType> = listOf(
        DamageType.DISEASE,
        DamageType.DROWNING,
        DamageType.POISON
    )
    override val resist: List<DamageType> = listOf(
        DamageType.FIRE,
        DamageType.COLD
    )
    override val vulnerableTo: List<DamageType> = listOf(
        DamageType.POUND
    )
    override val unarmedAttackVerb: String = "bite"
    override val unarmedDamageType: DamageType = DamageType.SLASH
    override val form: Form = Form.UNDEAD
    override val attributes: AttributesDAO =
        createStats(2, -1, 0, -3, 4, 0, 1)
    override val size: Size = Size.MEDIUM
    override val maxAppetite: Int = 5
    override val maxThirst: Int = 4
}
