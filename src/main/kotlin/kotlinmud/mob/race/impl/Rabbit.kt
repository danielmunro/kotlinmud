package kotlinmud.mob.race.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size
import org.jetbrains.exposed.sql.transactions.transaction

class Rabbit : Race {
    override val type: RaceType = RaceType.RABBIT
    override val playable: Boolean = false
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf()
    override val vulnerableTo: List<DamageType> = listOf()
    override val unarmedAttackVerb: String = "bite"
    override val unarmedDamageType: DamageType = DamageType.SLASH
    override val form: Form = Form.MAMMAL
    override val attributes: AttributesDAO = transaction { AttributesDAO.new {} }
    override val size: Size = Size.TINY
    override val maxAppetite: Int = 2
    override val maxThirst: Int = 2
}
