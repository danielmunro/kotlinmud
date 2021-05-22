package kotlinmud.action.impl.item

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.model.Affect
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.potionInInventory
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createQuaffAction(): Action {
    return ActionBuilder(Command.QUAFF).also {
        it.syntax = potionInInventory()
    } build { svc ->
        val item = svc.get<Item>(Syntax.AVAILABLE_POTION)
        val mob = svc.getMob()
        item.affects.forEach {
            mob.affects.add(Affect(it.type, it.timeout, it.attributes))
        }
        item.spells.forEach {
        }
        mob.items.remove(item)
        svc.createOkResponse(messageToActionCreator("you quaff $item"))
    }
}
