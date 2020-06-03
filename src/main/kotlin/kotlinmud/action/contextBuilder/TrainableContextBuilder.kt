package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.attributes.model.isVitals
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.player.service.PlayerService

class TrainableContextBuilder(private val mobService: MobService, private val playerService: PlayerService, private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val mobCard = playerService.findMobCardByName(mob.name)!!
        if (mobCard.trains == 0) {
            return Context(syntax, Status.ERROR, "you have no trains.")
        }
        val room = mobService.getRoomForMob(mob)
        mobService.getMobsForRoom(room).filter { it.job == JobType.TRAINER }.let {
            if (it.isEmpty()) {
                return Context(
                    syntax,
                    Status.ERROR,
                    "there is no trainer here."
                )
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
            else -> return Context(
                syntax,
                Status.ERROR,
                "you cannot train that"
            )
        }
        val amount = playerService.findMobCardByName(mob.name)!!.calcTrained(attribute)
        val maxAmount = if (isVitals(attribute)) 10 else 4
        if (amount == maxAmount) {
            return Context(
                syntax,
                Status.ERROR,
                "you cannot train that anymore."
            )
        }
        return Context(syntax, Status.OK, attribute)
    }
}
