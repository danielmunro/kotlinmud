package kotlinmud.mob.skill.type

import kotlinmud.action.type.Invokable
import kotlinmud.affect.type.Affect
import kotlinmud.helper.string.matches
import kotlinmud.io.service.RequestService
import kotlinmud.mob.type.HasCosts

interface SkillAction : Skill, Invokable, HasCosts {
    val affect: Affect?

    fun matchesRequest(request: RequestService): Boolean {
        return request.getCommand().matches(type.toString())
    }
}
