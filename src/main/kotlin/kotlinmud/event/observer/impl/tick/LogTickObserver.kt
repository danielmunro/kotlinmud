package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.logger
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService

class LogTickObserver(
    private val serverService: ServerService,
    private val mobService: MobService,
    private val roomService: RoomService,
    private val itemService: ItemService,
) : Observer {
    private val logger = logger(this)

    override suspend fun <T> invokeAsync(event: Event<T>) {
        logger.info(
            "tick :: {} clients, {} mobs, {} rooms, {} items",
            serverService.getClients().size,
            mobService.getMobCount(),
            roomService.getRoomCount(),
            itemService.getItemCount(),
        )
    }
}
