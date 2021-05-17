package kotlinmud.mob.race.impl

import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Sheep : Race() {
    override val type = RaceType.SHEEP
    override val unarmedAttackVerb = "chomp"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.MAMMAL
    override val size = Size.SMALL
    override val maxAppetite = 40
    override val maxThirst = 40
}
