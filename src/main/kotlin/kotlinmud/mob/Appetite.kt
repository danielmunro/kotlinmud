package kotlinmud.mob

import kotlinmud.mob.race.Race

class Appetite(race: Race) {
    private val maxHunger = race.maxAppetite
    private val maxThirst = race.maxThirst
    private var hunger = maxHunger
    private var thirst = maxThirst

    fun decrement() {
        if (hunger > -5) {
            hunger--
        }
        if (thirst > -5) {
            thirst--
        }
    }

    fun isThirsty(): Boolean {
        return thirst == -5
    }

    fun isHungry(): Boolean {
        return hunger == -5
    }

    fun isFull(): Boolean {
        return hunger == maxHunger || thirst == maxThirst
    }

    fun nourishThirst() {
        thirst = Math.max(1, thirst + 1)
    }

    fun nourishHunger() {
        hunger = Math.max(1, hunger + 1)
    }
}
