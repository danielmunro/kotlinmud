package kotlinmud.mob

import kotlinmud.mob.race.Race

class Appetite(race: Race) {
    private var appetite = race.maxAppetite
    private var thirst = race.maxThirst

    fun decrement() {
        if (appetite > 0) {
            appetite--
        }
        if (thirst > 0) {
            thirst--
        }
    }
}
