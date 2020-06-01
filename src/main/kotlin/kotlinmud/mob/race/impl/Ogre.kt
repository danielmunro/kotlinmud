package kotlinmud.mob.race.impl

import kotlinmud.attributes.createStats
import kotlinmud.attributes.model.Attributes
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

data class Ogre(override val type: RaceType = RaceType.OGRE) : Race {
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.POISON)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.MENTAL)
    override val unarmedAttackVerb: String = "punch"
    override val unarmedDamageType: DamageType = DamageType.POUND
    override val form: Form = Form.MAMMAL
    override val attributes: Attributes = createStats(2, -2, -2, 0, 2, 0, 1)
    override val size: Size = Size.LARGE
    override val maxAppetite: Int = 4
    override val maxThirst: Int = 4
}
