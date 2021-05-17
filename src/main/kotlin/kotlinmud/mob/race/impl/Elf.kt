package kotlinmud.mob.race.impl

import kotlinmud.attributes.factory.createStats
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size

class Elf : Race() {
    override val type = RaceType.ELF
    override val resist = listOf(DamageType.MENTAL)
    override val vulnerableTo = listOf(DamageType.FIRE, DamageType.COLD)
    override val unarmedAttackVerb = "scratch"
    override val unarmedDamageType = DamageType.SLASH
    override val form = Form.MAMMAL
    override val attributes = createStats(-2, 1, 2, 1, -2)
    override val size = Size.SMALL
    override val maxAppetite = 60
    override val maxThirst = 60
}
