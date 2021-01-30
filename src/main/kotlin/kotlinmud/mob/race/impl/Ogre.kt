package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Ogre : Race() {
    override val type = RaceType.OGRE
    override val resist = listOf(DamageType.POISON)
    override val vulnerableTo = listOf(DamageType.MENTAL)
    override val unarmedAttackVerb = "punch"
    override val unarmedDamageType = DamageType.POUND
    override val form = Form.MAMMAL
    override val attributes = createStats(2, -2, -2, 0, 2, 0, 1)
    override val size = Size.LARGE
    override val maxAppetite = 4
    override val maxThirst = 4
}
