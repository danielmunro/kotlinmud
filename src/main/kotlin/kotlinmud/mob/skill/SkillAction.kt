package kotlinmud.mob.skill

import kotlinmud.action.Invokable
import kotlinmud.affect.Affect
import kotlinmud.io.Request
import kotlinmud.string.matches

interface SkillAction : Skill, Invokable {
    val affect: Affect?

    fun matchesRequest(request: Request): Boolean {
        return matches(type.toString(), request.getCommand())
    }
}
