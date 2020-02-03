package kotlinmud.action

import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

class Action(
    private val command: Command,
    private val requiredDispositions: Array<Disposition>,
    private val syntax: Array<Syntax>,
    private val mutator: (input: String) -> Response,
    private val chainTo: Command = Command.NOOP
) {
}