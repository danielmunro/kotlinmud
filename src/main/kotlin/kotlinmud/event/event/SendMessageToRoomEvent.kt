package kotlinmud.event.event

import kotlinmud.io.Message
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class SendMessageToRoomEvent(
    val message: Message,
    val room: Room,
    val actionCreator: Mob,
    val target: Mob?
)
