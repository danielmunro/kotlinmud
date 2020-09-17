package kotlinmud.action.helper

import kotlinmud.action.model.ActionContextList
import kotlinmud.action.service.ActionContextService
import kotlinmud.event.service.EventService
import kotlinmud.io.service.RequestService
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.weather.service.WeatherService

fun createActionContextBuilder(
    mobService: MobService,
    playerService: PlayerService,
    itemService: ItemService,
    eventService: EventService,
    weatherService: WeatherService,
    serverService: ServerService
): (request: RequestService, actionContextList: ActionContextList) -> ActionContextService {
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
