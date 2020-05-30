package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.player.service.PlayerService
import kotlinmud.string.matches

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
        mob.skills.keys.find {
            matches(it.toString(), word)
        }?.let {
            if (mob.skills[it] == 100) {
                return Context(
                    syntax,
                    Status.ERROR,
                    "you cannot practice that anymore."
                )
            }
            return Context(syntax, Status.OK, it)
        }
        return Context(syntax, Status.ERROR, "you don't know that.")
    }
}
