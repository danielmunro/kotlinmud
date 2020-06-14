package kotlinmud.mob.model

import kotlinmud.mob.race.type.Race

class Appetite(
    val maxHunger: Int,
    val maxThirst: Int,
    hunger: Int? = null,
    thirst: Int? = null
) {
    companion object {
        fun fromRace(race: Race): Appetite {
            return Appetite(race.maxAppetite, race.maxThirst)
        }
    }

    private var hunger = hunger ?: maxHunger
    private var thirst = thirst ?: maxThirst

    fun getThirst(): Int {
        return thirst
    }

    fun getHunger(): Int {
        return hunger
    }

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
        thirst = Math.max(1, Math.min(maxThirst, thirst + 1))
    }

    fun nourishHunger(amount: Int) {
        hunger = Math.max(1, Math.min(maxHunger, hunger + amount))
    }
}
