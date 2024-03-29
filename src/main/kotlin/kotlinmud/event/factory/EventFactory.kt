package kotlinmud.event.factory

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.impl.SocialEvent
import kotlinmud.event.type.EventType
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob
import kotlinmud.player.social.Social
import kotlinmud.room.model.Room

fun createDeathEvent(mob: Mob): Event<SendMessageToRoomEvent> {
    return createSendMessageToRoomEvent(createDeathMessage(mob), mob.room, mob)
}

fun createSendMessageToRoomEvent(
    message: Message,
    room: Room,
    actionCreator: Mob,
    target: Mob? = null
): Event<SendMessageToRoomEvent> {
    return Event(
        EventType.SEND_MESSAGE_TO_ROOM,
        SendMessageToRoomEvent(message, room, actionCreator, target)
    )
}

fun createGameStartEvent(): Event<Any?> {
    return Event(EventType.GAME_START, null)
}

fun createGameLoopEvent(): Event<Any?> {
    return Event(EventType.GAME_LOOP, null)
}

fun createSocialEvent(social: Social): Event<SocialEvent> {
    return Event(EventType.SOCIAL, SocialEvent(social))
}

fun createTillEvent(room: Room): Event<Room> {
    return Event(EventType.TILL, room)
}

fun createRebootEvent(): Event<Any?> {
    return Event(EventType.REBOOT, null)
}
