package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Amphibian : Race() {
    override val type = RaceType.AVIAN
    override val unarmedAttackVerb = "kick"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.AMPHIBIAN
    override val size = Size.TINY
    override val maxAppetite = 30
    override val maxThirst = 30
}
