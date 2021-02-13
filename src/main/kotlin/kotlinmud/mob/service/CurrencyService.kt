package kotlinmud.mob.service

import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.type.CurrencyType

class CurrencyService(private val mob: MobDAO) {
    fun canAfford(item: ItemDAO): Boolean {
        var amountNeeded = item.worth
        amountNeeded -= mob.getCurrency(CurrencyType.Copper)
        if (amountNeeded > 0) {
            amountNeeded -= mob.getCurrency(CurrencyType.Silver) * 10
        }
        if (amountNeeded > 0) {
            amountNeeded -= mob.getCurrency(CurrencyType.Gold) * 100
        }
        return amountNeeded <= 0
    }

    fun transferTo(to: MobDAO, worth: Int) {
        var worthToAccountFor = worth
        val copperUsed = Math.min(worthToAccountFor, mob.getCurrency(CurrencyType.Copper))
        worthToAccountFor -= copperUsed
        val silverUsed = Math.min(worthToAccountFor / 10, mob.getCurrency(CurrencyType.Silver))
        worthToAccountFor -= silverUsed * 10
        val goldUsed = Math.min(worthToAccountFor / 100, mob.getCurrency(CurrencyType.Gold))
        worthToAccountFor -= goldUsed * 100

        if (worthToAccountFor == 0) {
            to.addCurrency(CurrencyType.Copper, copperUsed)
            to.addCurrency(CurrencyType.Silver, silverUsed)
            to.addCurrency(CurrencyType.Gold, goldUsed)
            mob.addCurrency(CurrencyType.Copper, -copperUsed)
            mob.addCurrency(CurrencyType.Silver, -silverUsed)
            mob.addCurrency(CurrencyType.Gold, -goldUsed)
        }
    }

    fun transferTo(to: MobDAO) {
        transferTo(to, mob.getCurrency(CurrencyType.Copper) + mob.getCurrency(CurrencyType.Silver) * 10 + mob.getCurrency(CurrencyType.Gold) * 100)
    }
}
