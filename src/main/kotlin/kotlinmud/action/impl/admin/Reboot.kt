package kotlinmud.action.impl.admin

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.mob.type.Role
import kotlinx.coroutines.runBlocking

fun createRebootAction(): Action {
    return ActionBuilder(Command.REBOOT).also {
        it.minimumRole = Role.Admin
    } build {
        runBlocking { it.publishReboot() }
        it.createOkResponse(messageToActionCreator("reboot initiated!"))
    }
}
