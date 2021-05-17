package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Lasher : Race() {
    override val type = RaceType.LASHER
    override val resist = listOf(DamageType.LIGHTNING, DamageType.LIGHT, DamageType.SOUND)
    override val vulnerableTo = listOf(DamageType.HOLY, DamageType.NEGATIVE)
    override val unarmedAttackVerb = "claw"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.MAMMAL
    override val attributes = createStats(1, -1, -2, 1, 1, 1, 0)
    override val size = Size.LARGE
    override val maxAppetite = 100
    override val maxThirst = 100
}
