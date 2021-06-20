package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Rodent : Race() {
    override val type = RaceType.RODENT
    override val unarmedAttackVerb = "chomp"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.MAMMAL
    override val size = Size.TINY
    override val maxAppetite = 10
    override val maxThirst = 10
}
