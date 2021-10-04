package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Insect : Race() {
    override val type = RaceType.INSECT
    override val resist = listOf(DamageType.DISEASE, DamageType.DROWNING)
    override val vulnerableTo = listOf(DamageType.POUND)
    override val unarmedAttackVerb = "bite"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.INSECT
    override val size = Size.TINY
    override val maxAppetite = 20
    override val maxThirst = 20
}
