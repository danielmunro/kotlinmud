package kotlinmud.event.factory

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.type.EventType
import kotlinmud.mob.dao.FightDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Round
import kotlinmud.mob.type.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createKillEvent(fight: FightDAO): Event<KillEvent> {
    val winner = transaction { if (fight.mob1.disposition == Disposition.DEAD) fight.mob2 else fight.mob1 }
    return Event(
        EventType.KILL,
        KillEvent(fight, winner, fight.getOpponentFor(winner)!!)
    )
}

fun createFightStartedEvent(fight: FightDAO, mob: MobDAO, target: MobDAO): Event<FightStartedEvent> {
    return Event(
        EventType.FIGHT_STARTED,
        FightStartedEvent(fight, mob, target)
    )
}

fun createFightRoundEvent(round: Round): Event<Round> {
    return Event(EventType.FIGHT_ROUND, round)
}
