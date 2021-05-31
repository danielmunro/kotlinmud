package kotlinmud.mob.model

import kotlinmud.mob.fight.type.FightStatus

class Fight(
    val mob1: Mob,
    val mob2: Mob,
) {
    private var status = FightStatus.FIGHTING

    fun isParticipant(mob: Mob): Boolean {
        return mob1 == mob || mob2 == mob
    }

    fun getOpponentFor(mob: Mob): Mob? {
        return if (mob == mob1) {
            mob2
        } else if (mob == mob2) {
            mob1
        } else {
            null
        }
    }

    fun isOver(): Boolean {
        return status == FightStatus.OVER
    }

    fun finish() {
        status = FightStatus.OVER
    }
}
