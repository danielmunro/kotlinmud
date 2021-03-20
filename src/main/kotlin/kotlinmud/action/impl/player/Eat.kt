package kotlinmud.action.impl.player

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createEatMessage
import kotlinmud.io.factory.foodInInventory
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.item.helper.applyAffectFromItem
import kotlinmud.item.model.Item

fun createEatAction(): Action {
    return ActionBuilder(Command.EAT).also {
        it.dispositions = mustBeAwake()
        it.syntax = foodInInventory()
    } build {
        val item = it.get<Item>(Syntax.AVAILABLE_FOOD)
        val mob = it.getMob()

        if (mob.isFull()) {
            return@build it.createErrorResponse(messageToActionCreator("you are full."))
        }

        mob.hunger += 1
        applyAffectFromItem(mob, item)
        mob.items.remove(item)

        it.createOkResponse(createEatMessage(mob, item))
    }
}
