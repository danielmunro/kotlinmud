package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.attributes.model.Attributes
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

data class Reptile(override val type: RaceType = RaceType.REPTILE) : Race {
    override val playable: Boolean = false
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.FIRE, DamageType.SLASH)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.COLD, DamageType.PIERCE)
    override val unarmedAttackVerb: String = "bite"
    override val unarmedDamageType: DamageType = DamageType.PIERCE
    override val form: Form = Form.REPTILE
    override val attributes: Attributes =
        createStats(0, 0, 0, 0, 0, 0, 1)
    override val size: Size = Size.SMALL
    override val maxAppetite: Int = 4
    override val maxThirst: Int = 4
}
