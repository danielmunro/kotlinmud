package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Kender : Race() {
    override val type = RaceType.KENDER
    override val vulnerableTo = listOf(DamageType.ENERGY)
    override val unarmedAttackVerb = "punch"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val attributes = createStats(-1, 0, 1, 2, 1, 1, 0)
    override val size = Size.MEDIUM
    override val maxAppetite = 2
    override val maxThirst = 3
}
