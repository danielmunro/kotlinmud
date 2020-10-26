package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.resource.service.ResourceService

class GrowResourcesObserver(private val resourceService: ResourceService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        resourceService.incrementMaturity()
    }
}
