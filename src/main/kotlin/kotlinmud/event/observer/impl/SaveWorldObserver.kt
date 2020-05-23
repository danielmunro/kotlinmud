package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.mob.MobService
import kotlinmud.player.PlayerService
import kotlinmud.service.PersistenceService
import kotlinmud.world.World

class SaveWorldObserver(
    private val persistenceService: PersistenceService,
    private val playerService: PlayerService,
    private val mobService: MobService,
    private val world: World
) : Observer {
    override val eventType: EventType = EventType.DAY

    override fun <T> processEvent(event: Event<T>) {
        persistenceService.writeAreas(world)
        playerService.persist()
        mobService.persistPlayerMobs()
    }
}
