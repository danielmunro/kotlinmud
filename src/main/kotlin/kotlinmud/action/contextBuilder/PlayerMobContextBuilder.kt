package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.player.service.PlayerService

class PlayerMobContextBuilder(private val playerService: PlayerService) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return playerService.findLoggedInPlayerMobByName(word)?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.ERROR, "they aren't here.")
    }
}
