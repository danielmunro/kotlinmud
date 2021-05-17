package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Felid : Race() {
    override val type = RaceType.FELID
    override val resist = listOf(DamageType.DISEASE)
    override val unarmedAttackVerb = "bite"
    override val unarmedDamageType = DamageType.PIERCE
    override val form = Form.MAMMAL
    override val size = Size.TINY
    override val maxAppetite = 60
    override val maxThirst = 40
}
