package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Undead : Race() {
    override val type = RaceType.UNDEAD
    override val immuneTo = listOf(
        DamageType.DISEASE,
        DamageType.DROWNING,
        DamageType.POISON
    )
    override val resist = listOf(
        DamageType.FIRE,
        DamageType.COLD
    )
    override val vulnerableTo = listOf(
        DamageType.POUND
    )
    override val unarmedAttackVerb = "bite"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.UNDEAD
    override val attributes = createStats(2, -1, 0, -3, 4, 0, 1)
    override val size = Size.MEDIUM
    override val maxAppetite = 5
    override val maxThirst = 4
}
