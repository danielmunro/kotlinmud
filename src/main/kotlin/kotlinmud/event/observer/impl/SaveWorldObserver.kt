package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.fs.service.PersistenceService
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.world.model.World

class SaveWorldObserver(
    private val persistenceService: PersistenceService,
    private val playerService: PlayerService,
    private val mobService: MobService,
    private val world: World
) : Observer {
    override val eventType: EventType = EventType.DAY

    override fun <T> processEvent(event: Event<T>) {
        persistenceService.writeWorld(world)
        playerService.persist()
        mobService.persistPlayerMobs()
    }
}
