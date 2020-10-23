package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.player.service.PlayerService

class LogOutAllPlayersOnStartupObserver(private val playerService: PlayerService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        playerService.logOutPlayers()
    }
}

fun logoutAllPlayersOnStartupEvent(playerService: PlayerService) {
    playerService.logOutPlayers()
}
