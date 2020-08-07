package kotlinmud.event.factory

import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.io.model.Message
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
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

fun createKillEvent(fight: Fight): Event<KillEvent> {
    val winner = fight.getWinner()!!
    return Event(EventType.KILL, KillEvent(fight, winner, fight.getOpponentFor(winner)!!))
}

fun createFightRoundEvent(round: Round): Event<Round> {
    return Event(EventType.FIGHT_ROUND, round)
}
