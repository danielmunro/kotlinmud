package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.service.ClientService
import kotlinmud.mob.model.PlayerMob

class TransferItemsOnKillObserver(private val clientService: ClientService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val killEvent = event.subject as KillEvent
        val victor = killEvent.victor
        val vanquished = killEvent.vanquished
        vanquished.items.addAll(vanquished.equipped)
        vanquished.equipped.clear()
        if (victor is PlayerMob && vanquished !is PlayerMob) {
            clientService.getClientForMob(victor)?.let {
                vanquished.items.forEach { item ->
                    it.write("you get $item from $vanquished's corpse.\n")
                }
            }
            victor.items.addAll(vanquished.items)
            vanquished.items.clear()
        }
    }
}
