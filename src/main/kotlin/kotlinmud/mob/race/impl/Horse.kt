package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Horse : Race() {
    override val type: RaceType = RaceType.HORSE
    override val unarmedAttackVerb = "wild kick"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val size = Size.LARGE
    override val maxAppetite = 160
    override val maxThirst = 160
}
