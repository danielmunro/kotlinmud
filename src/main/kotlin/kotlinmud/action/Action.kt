package kotlinmud.action

import kotlinmud.io.Buffer
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

class Action(
    val command: Command,
    private val requiredDispositions: Array<Disposition>,
    private val syntax: Array<Syntax>,
    val mutator: (Buffer) -> Response,
    private val chainTo: Command = Command.NOOP
) {
}