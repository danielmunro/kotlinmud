package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Deer : Race() {
    override val type = RaceType.DEER
    override val unarmedAttackVerb = "wild kick"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val size = Size.SMALL
    override val maxAppetite = 50
    override val maxThirst = 50
}
