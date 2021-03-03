package kotlinmud.event.observer.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.resource.service.ResourceService
import kotlinmud.room.model.Room

class TillRoomObserver(private val resourceService: ResourceService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Room) {
            substrateType = SubstrateType.TILLED_DIRT
            resources.forEach { resource ->
                if (resource == ResourceType.BRUSH) {
                    resourceService.tillResource(resource).forEach {
                        this.items.add(it)
                    }
                }
            }
        }
    }
}
