package kotlinmud.event.factory

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.type.EventType
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round

fun createKillEvent(fight: Fight): Event<KillEvent> {
    val winner = fight.getWinner()!!
    return Event(
        EventType.KILL,
        KillEvent(fight, winner, fight.getOpponentFor(winner)!!)
    )
}

fun createFightStartedEvent(fight: Fight, mob: MobDAO, target: MobDAO): Event<FightStartedEvent> {
    return Event(
        EventType.FIGHT_STARTED,
        FightStartedEvent(fight, mob, target)
    )
}

fun createFightRoundEvent(round: Round): Event<Round> {
    return Event(EventType.FIGHT_ROUND, round)
}
