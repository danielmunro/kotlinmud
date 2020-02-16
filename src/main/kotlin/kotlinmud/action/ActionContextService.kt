package kotlinmud.action

import kotlinmud.event.EventResponse
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.event.response.SendMessageToRoomResponse
import kotlinmud.io.Message
import kotlinmud.mob.MobEntity
import kotlinmud.room.RoomEntity
import kotlinmud.service.EventService
import kotlinmud.service.MobService

class ActionContextService(private val mobService: MobService, private val eventService: EventService) {
    fun getMobsInRoom(room: RoomEntity): List<MobEntity> {
        return mobService.getMobsForRoom(room)
    }

    fun moveMob(mob: MobEntity, room: RoomEntity) {
        mobService.moveMob(mob, room)
    }

    fun sendMessageToRoom(message: Message, room: RoomEntity, actionCreator: MobEntity, target: MobEntity? = null) {
        val resp: EventResponse<SendMessageToRoomResponse<SendMessageToRoomEvent>> =
            eventService.publish(createSendMessageToRoomEvent(message, room, actionCreator, target))
    }
}
