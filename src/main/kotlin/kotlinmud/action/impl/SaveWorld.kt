package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.io.factory.messageToActionCreator

fun createSaveWorldAction(worldSaver: WorldSaver): Action {
    return Action(Command.SAVE_WORLD) {
        worldSaver.save()
        it.createOkResponse(messageToActionCreator("world saved"))
    }
}
