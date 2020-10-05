package kotlinmud.event.observer.impl.tick

import kotlinmud.item.service.ItemService

fun decrementItemDecayTimerEvent(itemService: ItemService) {
    itemService.decrementDecayTimer()
}
