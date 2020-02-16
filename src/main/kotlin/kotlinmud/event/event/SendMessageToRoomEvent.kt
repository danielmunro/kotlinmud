package kotlinmud.event.event

import kotlinmud.io.Message
import kotlinmud.mob.MobEntity
import kotlinmud.room.RoomEntity

class SendMessageToRoomEvent(
    val message: Message,
    val room: RoomEntity,
    val actionCreator: MobEntity,
    val target: MobEntity?
)
