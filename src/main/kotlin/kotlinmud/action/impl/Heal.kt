package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.spellFromHealer
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO

fun createHealAction(): Action {
    return Action(Command.HEAL, mustBeStanding(), spellFromHealer()) {
        val skillType = it.get<MobDAO>(Syntax.SPELL_FROM_HEALER)
        it.createOkResponse(messageToActionCreator("success: $skillType"))
    }
}
