package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Human : Race() {
    override val type = RaceType.HUMAN
    override val unarmedAttackVerb = "punch"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val size = Size.MEDIUM
    override val maxAppetite = 3
    override val maxThirst = 3
}
