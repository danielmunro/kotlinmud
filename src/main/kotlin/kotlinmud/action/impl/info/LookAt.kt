package kotlinmud.action.impl.info

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.helper.Noun
import kotlinmud.io.factory.availableNoun
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob

fun createLookAtAction(): Action {
    return Action(Command.LOOK, mustBeAwake(), availableNoun()) {
        val noun = it.get<Noun>(Syntax.AVAILABLE_NOUN)
        val additionalDescription = when (noun) {
            is Item -> "\n\n${noun.name} is ${noun.material.value} and weighs ${noun.weight} pound${if (noun.weight == 1.0) "" else "s"}."
            is Mob -> "\n\n${noun.name} is a ${noun.race.type.toString().toLowerCase()}. ${noun.getHealthIndication()}"
            else -> ""
        }
        createResponseWithEmptyActionContext(
            messageToActionCreator(
                noun.description + additionalDescription
            )
        )
    }
}
