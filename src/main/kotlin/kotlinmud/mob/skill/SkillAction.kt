package kotlinmud.mob.skill

import kotlinmud.action.type.Invokable
import kotlinmud.affect.type.Affect
import kotlinmud.helper.string.matches
import kotlinmud.io.model.Request

interface SkillAction : Skill, Invokable {
    val affect: Affect?

    fun matchesRequest(request: Request): Boolean {
        return matches(type.toString(), request.getCommand())
    }
}
