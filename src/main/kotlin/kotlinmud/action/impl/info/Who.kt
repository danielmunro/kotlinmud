package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlive
import kotlinmud.io.messageToActionCreator

fun createWhoAction(): Action {
    return Action(Command.WHO, mustBeAlive()) { svc ->
        svc.createResponse(
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
