package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.createResponseWithEmptyActionContext

fun createAffectsAction(): Action {
    return Action(Command.AFFECTS) { svc ->
        createResponseWithEmptyActionContext(
            Message(
                "You are affected by:\n${svc.getAffects().fold("") { acc, it ->
                    acc + "${it.affectType.value}: ${it.timeout} tick${if (it.timeout == 1) "" else "s"}\n" }}"
            )
        )
    }
}
