package kotlinmud.mob.skill

import kotlinmud.io.model.Request

interface SpellAction : SkillAction {
    override fun matchesRequest(request: Request): Boolean {
        return request.args.size > 1 && kotlinmud.helper.string.matches(type.toString(), request.getSubject())
    }
}
