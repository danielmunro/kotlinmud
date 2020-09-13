package kotlinmud.event.impl

import kotlinmud.event.type.EventType
import kotlinmud.mob.fight.Round

class FightRoundEvent(round: Round) : Event<Round>(EventType.FIGHT_ROUND, round)
