package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Faerie : Race() {
    override val type: RaceType = RaceType.FAERIE
    override val resist = listOf(DamageType.MENTAL, DamageType.ENERGY)
    override val vulnerableTo = listOf(DamageType.POUND)
    override val unarmedAttackVerb = "slap"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val attributes = createStats(-3, 2, 2, 2, -3, 1, -1)
    override val size = Size.TINY
    override val maxAppetite = 60
    override val maxThirst = 60
}
