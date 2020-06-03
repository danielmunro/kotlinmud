package kotlinmud.action.impl.info

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlive
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator

fun createWhoAction(): Action {
    return Action(Command.WHO, mustBeAlive()) { svc ->
        svc.createOkResponse(
            messageToActionCreator(
                "MORTALS\n---------------\n" +
                        svc.getClients().fold("") { acc, it ->
                            acc +
                                    it.mob!!.level + " " +
                                    it.mob!!.race.type.toString().toLowerCase() + " " +
                                    it.mob!!.job.toString().toLowerCase() + " " +
                                    it.mob!!.name + "\n"
                        }
            )
        )
    }
}
