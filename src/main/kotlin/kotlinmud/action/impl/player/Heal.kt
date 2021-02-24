package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.spellFromHealer
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob

fun createHealAction(): Action {
    return Action(Command.HEAL, mustBeStanding(), spellFromHealer()) {
        val skillType = it.get<Mob>(Syntax.SPELL_FROM_HEALER)
        it.createOkResponse(messageToActionCreator("success: $skillType"))
    }
}
