package kotlinmud.action

import kotlinmud.action.model.ActionContextList
import kotlinmud.event.EventService
import kotlinmud.io.NIOServer
import kotlinmud.io.Request
import kotlinmud.item.ItemService
import kotlinmud.mob.MobService
import kotlinmud.player.PlayerService
import kotlinmud.service.WeatherService

fun createActionContextBuilder(
    mobService: MobService,
    playerService: PlayerService,
    itemService: ItemService,
    eventService: EventService,
    weatherService: WeatherService,
    server: NIOServer
): (request: Request, actionContextList: ActionContextList) -> ActionContextService {
    return { request, actionContextList ->
        ActionContextService(
            mobService,
            playerService,
            itemService,
            eventService,
            weatherService,
            actionContextList,
            server,
            request
        )
    }
}
