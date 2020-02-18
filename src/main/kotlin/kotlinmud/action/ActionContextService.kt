package kotlinmud.action

import kotlinmud.event.EventResponse
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import kotlinmud.service.EventService
import kotlinmud.service.MobService

class ActionContextService(
    private val mobService: MobService,
    private val eventService: EventService,
    private val actionContextList: ActionContextList
) {

    fun <T> get(syntax: Syntax): T {
        return actionContextList.getResultBySyntax(syntax)
    }

    fun getMobsInRoom(room: Room): List<Mob> {
        return mobService.getMobsForRoom(room)
    }

    fun moveMob(mob: Mob, room: Room) {
        mobService.moveMob(mob, room)
    }

    fun sendMessageToRoom(message: Message, room: Room, actionCreator: Mob, target: Mob? = null) {
        eventService.publish<SendMessageToRoomEvent, EventResponse<SendMessageToRoomEvent>>(
            createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    fun createResponse(message: Message): Response {
        return Response(actionContextList, message)
    }
}
