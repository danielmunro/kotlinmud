package kotlinmud.event.impl

import kotlinmud.io.model.Message
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO

data class SendMessageToRoomEvent(
    val message: Message,
    val room: RoomDAO,
    val actionCreator: MobDAO,
    val target: MobDAO?
)
