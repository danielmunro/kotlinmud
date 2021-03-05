package kotlinmud.action.helper

import kotlinmud.attributes.type.Attribute
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.Response
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.type.HasCosts

fun costApply(mob: Mob, hasCosts: HasCosts): Response? {
    val cost = hasCosts.costs.find {
        when (it.type) {
            CostType.MV_AMOUNT -> mob.mv < it.amount
            CostType.MV_PERCENT -> mob.mv < Math.max(mob.calc(Attribute.MV) * (it.amount.toDouble() / 100), 50.0)
            CostType.MANA_AMOUNT -> mob.mana < it.amount
            CostType.MANA_PERCENT -> mob.mana < mob.calc(Attribute.MANA) * (it.amount.toDouble() / 100)
            else -> false
        }
    }
    if (cost != null) {
        return createResponseWithEmptyActionContext(
            messageToActionCreator("You are too tired")
        )
    }
    hasCosts.costs.forEach {
        when (it.type) {
            CostType.DELAY -> return@forEach
            CostType.MV_AMOUNT -> mob.mv -= it.amount
            CostType.MV_PERCENT -> mob.mv -= (mob.calc(Attribute.MV) * (it.amount.toDouble() / 100)).toInt()
            CostType.MANA_AMOUNT -> mob.mana -= it.amount
            CostType.MANA_PERCENT -> mob.mana -= (mob.calc(Attribute.MANA) * (it.amount.toDouble() / 100)).toInt()
        }
    }
    return null
}
