package kotlinmud.event.impl

import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.RoomDAO

data class SendMessageToRoomEvent(
    val message: Message,
    val room: RoomDAO,
    val actionCreator: Mob,
    val target: Mob?
)
