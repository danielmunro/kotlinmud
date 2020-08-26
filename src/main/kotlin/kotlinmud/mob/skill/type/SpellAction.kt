package kotlinmud.mob.skill.type

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.helper.string.matches
import kotlinmud.io.factory.spell
import kotlinmud.io.model.Request

interface SpellAction : SkillAction {
    override fun matchesRequest(request: Request): Boolean {
        return request.args.size > 1 && request.getSubject().matches(type.toString())
    }

    override val invokesOn
        get() = SkillInvokesOn.INPUT

    override val command
        get() = Command.CAST

    override val dispositions
        get() = mustBeAlert()

    override val syntax
        get() = spell()

    override val argumentOrder
        get() = listOf(0, 1, 2)
}
