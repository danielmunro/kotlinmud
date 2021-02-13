package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.service.CurrencyService

class TransferGoldOnKillObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val killEvent = event.subject as KillEvent
        val currencyService = CurrencyService(killEvent.vanquished)
        currencyService.transferTo(killEvent.victor)
    }
}
