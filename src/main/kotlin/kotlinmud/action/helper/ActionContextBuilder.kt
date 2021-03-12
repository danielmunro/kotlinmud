package kotlinmud.action.helper

import kotlinmud.action.model.ActionContextList
import kotlinmud.action.service.ActionContextService
import kotlinmud.event.service.EventService
import kotlinmud.io.service.RequestService
import kotlinmud.io.service.ServerService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.quest.service.QuestService
import kotlinmud.room.service.RoomService
import kotlinmud.time.service.TimeService
import kotlinmud.weather.service.WeatherService

fun createActionContextBuilder(
    mobService: MobService,
    itemService: ItemService,
    roomService: RoomService,
    eventService: EventService,
    weatherService: WeatherService,
    serverService: ServerService,
    questService: QuestService,
    timeService: TimeService,
): (request: RequestService, actionContextList: ActionContextList) -> ActionContextService {
    return { request, actionContextList ->
        ActionContextService(
            mobService,
            itemService,
            roomService,
            eventService,
            weatherService,
            actionContextList,
            serverService,
            request,
            questService,
            timeService,
        )
    }
}
