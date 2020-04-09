package kotlinmud.saver.mapper

import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.mob.SpecializationType

fun mapMob(mob: Mob): String {
    return """#${mob.id}
${mob.name}~
${mob.brief}~
${mob.description}~
${mob.disposition.value}~
""" +
            optional(mob.job != JobType.NONE, "job: ${mob.job.value}") +
            optional(mob.specialization != SpecializationType.NONE, "specialization: ${mob.specialization.value}") +
            optional(mob.route.isNotEmpty(), "route: ${mob.route.joinToString("-")}") +
            optional(mob.goldMin > 0, "goldMin: ${mob.goldMin}") +
            optional(mob.goldMax > 0, "goldMax: ${mob.goldMax}") +
            "~\n" +
            mob.affects.joinToString { it.affectType.value } + "~"
}
