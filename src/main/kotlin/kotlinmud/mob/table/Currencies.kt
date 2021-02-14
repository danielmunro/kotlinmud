package kotlinmud.mob.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Currencies : IntIdTable() {
    val currencyType = varchar("currencyType", 255)
    val amount = integer("amount")
    val mobId = reference("mob", Mobs, ReferenceOption.CASCADE)
}
