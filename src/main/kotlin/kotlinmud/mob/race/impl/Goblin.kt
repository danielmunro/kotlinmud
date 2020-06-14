package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.attributes.model.Attributes
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

data class Goblin(override val type: RaceType = RaceType.GOBLIN) : Race {
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf(DamageType.POISON)
    override val resist: List<DamageType> = listOf(DamageType.DISEASE)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.LIGHTNING)
    override val unarmedAttackVerb: String = "bite"
    override val unarmedDamageType: DamageType = DamageType.PIERCE
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes =
        createStats(0, -1, -1, 1, 2, 0, 1)
    override val size: Size = Size.LARGE
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 2
}
