package kotlinmud.action.helper

import kotlinmud.action.model.ActionContextList
import kotlinmud.action.service.ActionContextService
import kotlinmud.event.service.EventService
import kotlinmud.io.model.Request
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.WeatherService

fun createActionContextBuilder(
    mobService: MobService,
    playerService: PlayerService,
    itemService: ItemService,
    eventService: EventService,
    weatherService: WeatherService,
    serverService: ServerService
): (request: Request, actionContextList: ActionContextList) -> ActionContextService {
    return { request, actionContextList ->
        ActionContextService(
            mobService,
            playerService,
            itemService,
            eventService,
            weatherService,
            actionContextList,
            serverService,
            request
        )
    }
}
