package kotlinmud.event.factory

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.impl.SocialEvent
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Message
import kotlinmud.mob.dao.MobDAO
import kotlinmud.player.social.Social
import kotlinmud.room.dao.RoomDAO

fun createSendMessageToRoomEvent(
    message: Message,
    room: RoomDAO,
    actionCreator: MobDAO,
    target: MobDAO? = null
): Event<SendMessageToRoomEvent> {
    return Event(
        EventType.SEND_MESSAGE_TO_ROOM,
        SendMessageToRoomEvent(message, room, actionCreator, target)
    )
}

fun createGameStartEvent(): Event<Any?> {
    return Event(EventType.GAME_START, null)
}

fun createSocialEvent(social: Social): Event<SocialEvent> {
    return Event(EventType.SOCIAL, SocialEvent(social))
}
