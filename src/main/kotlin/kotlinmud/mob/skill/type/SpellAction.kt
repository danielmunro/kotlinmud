package kotlinmud.mob.skill.type

import kotlinmud.helper.string.matches
import kotlinmud.io.model.Request

interface SpellAction : SkillAction {
    override fun matchesRequest(request: Request): Boolean {
        return request.args.size > 1 && request.getSubject().matches(type.toString())
    }
}
