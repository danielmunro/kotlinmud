package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Lizard : Race() {
    override val type = RaceType.LIZARD
    override val resist = listOf(DamageType.FIRE)
    override val vulnerableTo = listOf(DamageType.COLD)
    override val unarmedAttackVerb = "bite"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.REPTILE
    override val size = Size.TINY
    override val maxAppetite = 20
    override val maxThirst = 20
}
