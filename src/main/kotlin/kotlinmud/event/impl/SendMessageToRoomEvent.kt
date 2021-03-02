package kotlinmud.event.impl

import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Room

data class SendMessageToRoomEvent(
    val message: Message,
    val room: Room,
    val actionCreator: Mob,
    val target: Mob?
)
