package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createSleepMessage
import kotlinmud.io.factory.optionalFurniture
import kotlinmud.mob.type.Disposition

fun createSleepAction(): Action {
    return Action(
        Command.SLEEP,
        listOf(Disposition.STANDING, Disposition.SITTING),
        optionalFurniture()
    ) {
        it.getMob().disposition = Disposition.SLEEPING
        it.createOkResponse(createSleepMessage(it.getMob()))
    }
}
