package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService

class DecrementItemDecayTimerObserver(
        private val mobService: MobService,
        private val roomService: RoomService,
        private val itemService: ItemService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        itemService.decrementDecayTimer()
        roomService.removeDecayedItems()
        mobService.removeDecayedItems()
    }
}
