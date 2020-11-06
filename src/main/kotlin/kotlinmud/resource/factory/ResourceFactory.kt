package kotlinmud.resource.factory

import kotlinmud.biome.type.ResourceType
import kotlinmud.room.dao.ResourceDAO
import org.jetbrains.exposed.sql.transactions.transaction

fun createWildGrass(): ResourceDAO {
    return transaction {
        ResourceDAO.new {
            name = "wild grass"
            type = ResourceType.BRUSH
            isPlanted = true
        }
    }
}
