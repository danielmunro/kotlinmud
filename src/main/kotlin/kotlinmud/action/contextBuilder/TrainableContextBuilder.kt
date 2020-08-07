package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.repository.findMobsForRoom
import kotlinmud.mob.type.JobType
import kotlinmud.player.service.PlayerService
import org.jetbrains.exposed.sql.transactions.transaction

class TrainableContextBuilder(private val playerService: PlayerService, private val mob: MobDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val mobCard = playerService.findMobCardByName(mob.name)!!
        if (mobCard.trains == 0) {
            return Context(syntax, Status.ERROR, "you have no trains.")
        }
        val room = transaction { mob.room }
        findMobsForRoom(room).filter { it.job == JobType.TRAINER }.let {
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
        val maxAmount = if (attribute.isVitals()) 10 else 4
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
