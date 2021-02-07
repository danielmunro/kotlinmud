package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Bear : Race() {
    override val type = RaceType.BEAR
    override val resist = listOf(
        DamageType.POUND,
        DamageType.SLASH,
        DamageType.PIERCE
    )
    override val unarmedAttackVerb = "swipe"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.MAMMAL
    override val attributes = createStats(2, -2, -2, 0, 2)
    override val size = Size.LARGE
    override val maxAppetite = 3
    override val maxThirst = 3
}
