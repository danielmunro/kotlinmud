package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService

class SavePlayerMobsObserver(
    private val mobService: MobService,
    private val playerService: PlayerService
) : Observer {

    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobService.findPlayerMobs().forEach {
            playerService.dumpPlayerMobData(it)
        }
    }
}
