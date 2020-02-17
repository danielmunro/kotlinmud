package kotlinmud.action

import kotlinmud.event.EventResponse
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.MobEntity
import kotlinmud.room.RoomEntity
import kotlinmud.service.EventService
import kotlinmud.service.MobService

class ActionContextService(
    private val mobService: MobService,
    private val eventService: EventService,
    private val actionContextList: ActionContextList) {

    fun <T> get(syntax: Syntax): T {
        return actionContextList.getResultBySyntax(syntax)
    }

    fun getMobsInRoom(room: RoomEntity): List<MobEntity> {
        return mobService.getMobsForRoom(room)
    }

    fun moveMob(mob: MobEntity, room: RoomEntity) {
        mobService.moveMob(mob, room)
    }

    fun sendMessageToRoom(message: Message, room: RoomEntity, actionCreator: MobEntity, target: MobEntity? = null) {
        eventService.publish<SendMessageToRoomEvent, EventResponse<SendMessageToRoomEvent>>(
            createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    fun createResponse(message: Message): Response {
        return Response(actionContextList, message)
    }
}
