package kotlinmud.mob.race.impl

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.createStats
import kotlinmud.mob.Form
import kotlinmud.mob.Size
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType

class Goblin : Race {
    override val type: RaceType = RaceType.GOBLIN
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf(DamageType.POISON)
    override val resist: List<DamageType> = listOf(DamageType.DISEASE)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.LIGHTNING)
    override val unarmedAttackVerb: String = "bite"
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(0, -1, -1, 1, 2, 0, 1)
    override val size: Size = Size.LARGE
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 2
}
