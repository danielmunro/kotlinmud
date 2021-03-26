package kotlinmud.action.impl

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.spell
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.type.Spell

fun createCastAction(): Action {
    return ActionBuilder(Command.CAST).also {
        it.syntax = spell()
    } build {
        val spell = it.get<Spell>(Syntax.SPELL)
        val mob = it.getMob()
        val target = it.get(Syntax.OPTIONAL_TARGET) ?: mob
        it.createOkResponse(spell.createMessage(mob, target))
    }
}
