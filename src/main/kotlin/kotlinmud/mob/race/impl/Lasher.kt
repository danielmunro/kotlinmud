package kotlinmud.mob.race.impl

import kotlinmud.attributes.createStats
import kotlinmud.attributes.model.Attributes
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Lasher : Race {
    override val type: RaceType = RaceType.LASHER
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.LIGHTNING, DamageType.LIGHT, DamageType.SOUND)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.HOLY, DamageType.NEGATIVE)
    override val unarmedAttackVerb: String = "claw"
    override val unarmedDamageType: DamageType = DamageType.SLASH
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(1, -1, -2, 1, 1, 1, 0)
    override val size: Size = Size.LARGE
    override val maxAppetite: Int = 4
    override val maxThirst: Int = 3
}
