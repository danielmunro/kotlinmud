package kotlinmud.mob.skill.factory

import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType

fun delayCostOf(amount: Int): Cost {
    return Cost(CostType.DELAY, amount)
}

fun manaCostOf(amount: Int): Cost {
    return Cost(CostType.MANA_AMOUNT, amount)
}

fun defaultSpellCost(): List<Cost> {
    return listOf(
        delayCostOf(1),
        manaCostOf(50)
    )
}
