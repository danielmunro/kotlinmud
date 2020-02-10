package kotlinmud.action

import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

class Action(
    val command: Command,
    private val requiredDispositions: Array<Disposition>,
    val syntax: Array<Syntax>,
    val mutator: (ActionContextService, ContextCollection, Request) -> Response,
    val chainTo: Command = Command.NOOP
) {
    fun hasDisposition(disposition: Disposition): Boolean {
        return requiredDispositions.contains(disposition)
    }

    fun isChained(): Boolean {
        return chainTo != Command.NOOP
    }
}