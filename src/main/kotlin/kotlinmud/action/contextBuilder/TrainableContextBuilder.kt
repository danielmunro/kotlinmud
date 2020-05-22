package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.isVitals
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import kotlinmud.mob.type.JobType
import kotlinmud.service.MobService

class TrainableContextBuilder(private val mobService: MobService, private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        if (mob.trains == 0) {
            return Context(syntax, Status.ERROR, "you have no trains.")
        }
        val room = mobService.getRoomForMob(mob)
        mobService.getMobsForRoom(room).filter { it.job == JobType.TRAINER }.let {
            if (it.isEmpty()) {
                return Context(syntax, Status.ERROR, "there is no trainer here.")
            }
        }
        val attribute = when (word) {
            "str" -> Attribute.STR
            "int" -> Attribute.INT
            "wis" -> Attribute.WIS
            "dex" -> Attribute.DEX
            "con" -> Attribute.CON
            "hp" -> Attribute.HP
            "mana" -> Attribute.MANA
            "mv" -> Attribute.MV
            else -> return Context(syntax, Status.ERROR, "you cannot train that")
        }
        val amount = mob.calcTrained(attribute)
        val maxAmount = if (isVitals(attribute)) 10 else 4
        if (amount == maxAmount) {
            return Context(syntax, Status.ERROR, "you cannot train that anymore.")
        }
        return Context(syntax, Status.OK, attribute)
    }
}
