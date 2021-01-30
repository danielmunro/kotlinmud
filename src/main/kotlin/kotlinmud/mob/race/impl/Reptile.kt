package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Reptile : Race() {
    override val type = RaceType.REPTILE
    override val resist = listOf(DamageType.FIRE, DamageType.SLASH)
    override val vulnerableTo = listOf(DamageType.COLD, DamageType.PIERCE)
    override val unarmedAttackVerb = "bite"
    override val unarmedDamageType = DamageType.PIERCE
    override val form = Form.REPTILE
    override val attributes = createStats(0, 0, 0, 0, 0, 0, 1)
    override val size = Size.SMALL
    override val maxAppetite = 4
    override val maxThirst = 4
}
