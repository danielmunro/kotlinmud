package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Syntax
import kotlinmud.io.messageToActionCreator
import kotlinmud.io.spellFromHealer
import kotlinmud.mob.Mob

fun createHealAction(): Action {
    return Action(Command.HEAL, mustBeStanding(), spellFromHealer()) {
        val skillType = it.get<Mob>(Syntax.SPELL_FROM_HEALER)
        it.createResponse(messageToActionCreator("success: $skillType"))
    }
}
