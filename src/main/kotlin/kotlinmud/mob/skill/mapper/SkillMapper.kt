package kotlinmud.mob.skill.mapper

import kotlinmud.fs.helper.end
import kotlinmud.fs.helper.str
import kotlinmud.mob.skill.type.SkillType

fun mapSkills(skills: Map<SkillType, Int>): String {
    return skills.entries.joinToString { str("${it.key} ${it.value}") } + end()
}
