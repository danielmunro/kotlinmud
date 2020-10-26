package kotlinmud.event.observer.impl.tick

import kotlinmud.biome.type.ResourceType
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.repository.findRoomsForGrassGeneration
import org.jetbrains.exposed.sql.transactions.transaction

class GenerateGrassObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
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
