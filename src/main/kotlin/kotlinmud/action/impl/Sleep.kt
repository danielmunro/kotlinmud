package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.optionalFurniture
import kotlinmud.mob.Disposition

fun createSleepAction(): Action {
    return Action(Command.SLEEP, listOf(Disposition.STANDING, Disposition.SITTING), optionalFurniture()) {
        it.getMob().disposition = Disposition.SLEEPING
//        val furniture = it.get<Item>(Syntax.OPTIONAL_FURNITURE) @todo sleeping in/on furniture

        it.createResponse(
            MessageBuilder()
                .toActionCreator("you lay down and go to sleep.")
                .toObservers("${it.getMob()} lays down and goes to sleep.")
                .build()
        )
    }
}
