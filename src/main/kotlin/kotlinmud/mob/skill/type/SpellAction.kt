package kotlinmud.mob.skill.type

import kotlinmud.io.model.Request
import kotlinmud.mob.skill.type.SkillAction

interface SpellAction : SkillAction {
    override fun matchesRequest(request: Request): Boolean {
        return request.args.size > 1 && kotlinmud.helper.string.matches(type.toString(), request.getSubject())
    }
}
