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

fun weightMessage(item: Item): String {
    return if (item.weight > 0)
        " It weighs ${item.weight} pound${if (item.weight == 1.0) "" else "s"}"
    else
        ""
}

fun inventoryMessage(item: Item): String {
    val itemsReduced = item.items?.fold("Items:\n") { acc, it -> acc + it.name + "\n" }
    return if (item.isContainer)
        "\n\n$itemsReduced"
    else
        ""
}

fun createLookAtAction(): Action {
    return Action(Command.LOOK, mustBeAwake(), availableNoun()) {
        val noun = it.get<Noun>(Syntax.AVAILABLE_NOUN)
        val additionalDescription = when (noun) {
            is Item -> "\n${noun.name} is ${noun.material.value}.${weightMessage(noun)}${inventoryMessage(noun)}"
            is Mob -> "\n${noun.description}\n${noun.equipped.fold("") { acc, item -> "$acc$item\n" }}\n${noun.getHealthIndication()}"
            else -> ""
        }
        createResponseWithEmptyActionContext(
            messageToActionCreator(
                noun.description + additionalDescription
            )
        )
    }
}
