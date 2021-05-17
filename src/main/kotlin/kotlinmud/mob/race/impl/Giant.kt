package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Giant : Race() {
    override val type = RaceType.GIANT
    override val resist = listOf(DamageType.FIRE, DamageType.COLD)
    override val vulnerableTo = listOf(DamageType.ENERGY, DamageType.MENTAL)
    override val unarmedAttackVerb = "punch"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val attributes = createStats(3, -2, -2, -2, 3, 0, 1)
    override val size = Size.HUGE
    override val maxAppetite = 240
    override val maxThirst = 240
}
