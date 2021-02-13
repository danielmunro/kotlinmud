package kotlinmud.mob.dao

import kotlinmud.mob.table.Currencies
import kotlinmud.mob.type.CurrencyType
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class CurrencyDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CurrencyDAO>(Currencies)

    var currencyType by Currencies.currencyType.transform(
        { it.toString() },
        { CurrencyType.valueOf(it) }
    )
    var amount by Currencies.amount
    var mob by MobDAO referencedOn Currencies.mobId
}