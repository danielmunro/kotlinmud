package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Dwarf : Race() {
    override val type = RaceType.DWARF
    override val resist = listOf(DamageType.POUND)
    override val vulnerableTo = listOf(DamageType.DROWNING)
    override val unarmedAttackVerb = "punch"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val attributes = createStats(2, -2, 0, -1, 1, 0, 1)
    override val size = Size.SMALL
    override val maxAppetite = 3
    override val maxThirst = 3
}
