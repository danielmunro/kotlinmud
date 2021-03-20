package kotlinmud.action.impl.info

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator

fun createScanAction(): Action {
    return ActionBuilder(Command.SCAN) build { svc ->
        val playerMobs = svc.getPlayerMobsInArea()

        if (playerMobs.isEmpty()) {
            return@build svc.createOkResponse(messageToActionCreator("You found no players nearby."))
        }

        svc.createOkResponse(
            messageToActionCreator(
                "Players found:\n" + playerMobs.fold("") { acc, it -> acc + it.name + " is at " + it.room.name + "\n" }
            )
        )
    }
}
