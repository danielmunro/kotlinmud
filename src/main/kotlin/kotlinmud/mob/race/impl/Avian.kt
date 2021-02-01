package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Avian : Race() {
    override val type = RaceType.AVIAN
    override val unarmedAttackVerb = "peck"
    override val unarmedDamageType = DamageType.PIERCE
    override val form = Form.BIRD
    override val size = Size.TINY
    override val maxAppetite = 3
    override val maxThirst = 3
}
