package kotlinmud.item.factory

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import org.jetbrains.exposed.sql.transactions.transaction

fun createGrassSeeds(): ItemDAO {
    return transaction {
        ItemDAO.new {
            name = "small green seeds"
            description = "a handful of small green seeds are here"
            type = ItemType.GRASS_SEED
        }
    }
}
