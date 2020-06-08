package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.Observer
import kotlinmud.event.type.EventType
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.PersistenceService
import kotlinmud.world.model.World

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
