package kotlinmud.event.factory

import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.impl.PlayerLoggedInEvent
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.impl.SocialEvent
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.io.model.Message
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.social.Social
import kotlinmud.room.dao.RoomDAO

fun createClientConnectedEvent(client: Client): Event<ClientConnectedEvent> {
    return Event(EventType.CLIENT_CONNECTED, ClientConnectedEvent(client))
}

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

fun createClientDisconnectedEvent(client: Client): Event<Client> {
    return Event(EventType.CLIENT_DISCONNECTED, client)
}

fun createGameStartEvent(): Event<Any?> {
    return Event(EventType.GAME_START, null)
}

fun createSocialEvent(social: Social): Event<SocialEvent> {
    return Event(EventType.SOCIAL, SocialEvent(social))
}

fun createClientLoggedInEvent(client: Client, mobCard: MobCardDAO): Event<PlayerLoggedInEvent> {
    return Event(
        EventType.CLIENT_LOGGED_IN,
        PlayerLoggedInEvent(client, mobCard)
    )
}
