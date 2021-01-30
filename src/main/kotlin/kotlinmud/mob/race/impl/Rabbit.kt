package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.emptyAttributes
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Rabbit : Race() {
    override val type = RaceType.RABBIT
    override val unarmedAttackVerb = "bite"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.MAMMAL
    override val size = Size.TINY
    override val maxAppetite = 2
    override val maxThirst = 2
}
