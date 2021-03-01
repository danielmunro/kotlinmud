package kotlinmud.event.observer.impl

import kotlinmud.biome.type.SubstrateType
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.resource.service.ResourceService
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class TillRoomObserver(private val resourceService: ResourceService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as RoomDAO) {
            transaction {
                substrate = SubstrateType.TILLED_DIRT
                resources.forEach { resource ->
                    if (resource.isPlanted) {
                        resourceService.tillResource(resource).forEach {
                            this@with.items.plus(it)
                        }
                    }
                }
            }
        }
    }
}
