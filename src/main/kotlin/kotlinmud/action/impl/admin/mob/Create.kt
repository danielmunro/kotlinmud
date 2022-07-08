package kotlinmud.action.impl.admin.mob

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createMobCreateMessage
import kotlinmud.io.factory.subcommandWithFreeform
import kotlinmud.io.type.Syntax

fun createMobCreateAction(): Action {
    return ActionBuilder(Command.MOB_CREATE).also {
        it.syntax = subcommandWithFreeform()
    } build {
        val name = it.get<String>(Syntax.FREE_FORM)
        it.createMob(name)
        it.createOkResponse(createMobCreateMessage(name))
    }
}
