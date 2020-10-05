package kotlinmud.event.observer.impl.tick

import kotlinmud.io.service.ClientService

fun decrementDelayEvent(clientService: ClientService) {
    clientService.decrementDelays()
}
