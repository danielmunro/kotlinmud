import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.type.Form
import kotlinmud.mob.type.Size
import org.jetbrains.exposed.sql.transactions.transaction

class Horse : Race {
    override val type: RaceType = RaceType.HORSE
    override val playable: Boolean = false
    override val immuneTo: List<DamageType> = listOf()
    override val resist: List<DamageType> = listOf()
    override val vulnerableTo: List<DamageType> = listOf()
    override val unarmedAttackVerb: String = "wild kick"
    override val unarmedDamageType: DamageType = DamageType.POUND
    override val form: Form = Form.MAMMAL
    override val attributes: AttributesDAO = transaction { AttributesDAO.new {} }
    override val size: Size = Size.LARGE
    override val maxAppetite: Int = 3
    override val maxThirst: Int = 3
}
