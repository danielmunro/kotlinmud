package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

data class Elf(override val type: RaceType = RaceType.ELF) : Race {
    override val playable: Boolean = true
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf(DamageType.MENTAL)
    override val vulnerableTo: List<DamageType> = listOf(DamageType.FIRE, DamageType.COLD)
    override val unarmedAttackVerb: String = "scratch"
    override val unarmedDamageType: DamageType = DamageType.SLASH
    override val form: Form = Form.MAMMAL
    override val attributes: AttributesDAO = createStats(-2, 1, 2, 1, -2)
    override val size: Size = Size.SMALL
    override val maxAppetite: Int = 2
    override val maxThirst: Int = 2
}
