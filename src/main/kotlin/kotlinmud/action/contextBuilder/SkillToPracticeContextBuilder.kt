package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.Mob
import kotlinmud.player.service.PlayerService

class SkillToPracticeContextBuilder(private val playerService: PlayerService, private val mob: MobDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val mobCard = playerService.findMobCardByName(mob.name)!!
        if (mobCard.practices == 0) {
            return Context(
                syntax,
                Status.ERROR,
                "you don't have any practices left."
            )
        }
        mob.skills.find {
            matches(it.type.toString(), word)
        }?.let {
            if (it.level == 100) {
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
