package kotlinmud.event.observer.impl

import kotlinmud.player.service.PlayerService

fun logoutAllPlayersOnStartupEvent(playerService: PlayerService) {
    playerService.logOutPlayers()
}
