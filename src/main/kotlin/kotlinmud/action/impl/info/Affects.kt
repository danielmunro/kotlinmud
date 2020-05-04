package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext

fun createAffectsAction(): Action {
    return Action(
        Command.AFFECTS,
        mustBeAlert(),
        listOf(Syntax.COMMAND)) { svc ->
            createResponseWithEmptyActionContext(
                Message(
                    "You are affected by:\n${svc.getMob().affects.fold("") { acc, it ->
                        acc + "${it.affectType.value}: ${it.timeout} tick${if (it.timeout == 1) "" else "s"}\n" }}"
                )
            )
        }
}
