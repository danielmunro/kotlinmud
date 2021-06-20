package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Cow : Race() {
    override val type = RaceType.COW
    override val unarmedAttackVerb = "chomp"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val size = Size.LARGE
    override val maxAppetite = 40
    override val maxThirst = 40
}
