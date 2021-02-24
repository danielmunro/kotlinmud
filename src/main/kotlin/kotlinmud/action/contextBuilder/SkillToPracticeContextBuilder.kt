package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.player.service.PlayerService
import org.jetbrains.exposed.sql.transactions.transaction

class SkillToPracticeContextBuilder(private val playerService: PlayerService, private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val mobCard = playerService.findMobCardByName(mob.name)!!
        if (mobCard.practices == 0) {
            return Context(
                syntax,
                Status.ERROR,
                "you don't have any practices left."
            )
        }
        return transaction {
            mob.skills.keys.find {
                word.matches(it.toString())
            }?.let {
                if (mob.skills[it] == 100) {
                    return@transaction Context(
                        syntax,
                        Status.ERROR,
                        "you cannot practice that anymore."
                    )
                }
                return@transaction Context(syntax, Status.OK, it)
            }
            return@transaction Context(syntax, Status.ERROR, "you don't know that.")
        }
    }
}
