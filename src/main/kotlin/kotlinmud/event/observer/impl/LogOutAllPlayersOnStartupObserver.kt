package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.player.service.PlayerService

class LogOutAllPlayersOnStartupObserver(private val playerService: PlayerService) : Observer {
    override val eventType: EventType = EventType.GAME_START

    override fun <T> processEvent(event: Event<T>) {
        playerService.logOutPlayers()
    }
}
