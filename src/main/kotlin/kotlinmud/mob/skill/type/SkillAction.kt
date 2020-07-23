package kotlinmud.mob.skill.type

import kotlinmud.action.type.Invokable
import kotlinmud.affect.type.Affect
import kotlinmud.helper.string.matches
import kotlinmud.io.model.Request
import kotlinmud.mob.type.HasCosts

interface SkillAction : Skill, Invokable, HasCosts {
    val affect: Affect?

    fun matchesRequest(request: Request): Boolean {
        return matches(type.toString(), request.getCommand())
    }
}
