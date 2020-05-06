package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.io.Message

fun createSaveWorldAction(worldSaver: WorldSaver): Action {
    return Action(Command.SAVE_WORLD, mustBeAlert()) {
        worldSaver.save()
        it.createResponse(Message("world saved"))
    }
}
