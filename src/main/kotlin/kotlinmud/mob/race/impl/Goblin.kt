package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Goblin : Race() {
    override val type = RaceType.GOBLIN
    override val immuneTo = listOf(DamageType.POISON)
    override val resist = listOf(DamageType.DISEASE)
    override val vulnerableTo = listOf(DamageType.LIGHTNING)
    override val unarmedAttackVerb = "bite"
    override val unarmedDamageType = DamageType.PIERCE
    override val form = Form.MAMMAL
    override val attributes = createStats(0, -1, -1, 1, 2, 0, 1)
    override val size = Size.LARGE
    override val maxAppetite = 3
    override val maxThirst = 2
}
