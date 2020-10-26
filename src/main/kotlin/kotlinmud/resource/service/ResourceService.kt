package kotlinmud.resource.service

import kotlinmud.biome.type.ResourceType
import kotlinmud.resource.repository.incrementResourceMaturity
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.repository.findRoomsForGrassGeneration
import org.jetbrains.exposed.sql.transactions.transaction

class ResourceService {
    fun incrementMaturity() {
        incrementResourceMaturity()
    }

    fun generateGrass() {
        findRoomsForGrassGeneration().forEach {
            transaction {
                ResourceDAO.new {
                    type = ResourceType.BRUSH
                    name = "grass"
                    room = it
                }
            }
        }
    }
}
