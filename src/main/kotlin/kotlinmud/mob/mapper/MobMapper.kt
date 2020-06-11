package kotlinmud.mob.mapper

import kotlinmud.affect.mapper.mapAffects
import kotlinmud.attributes.mapper.mapAttributes
import kotlinmud.fs.helper.int
import kotlinmud.fs.helper.str
import kotlinmud.fs.saver.mapper.optional
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.mapper.mapSkills
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.SpecializationType

fun mapMob(mob: Mob): String {
    val optional = optional(
        mob.job != JobType.NONE,
        "job: ${mob.job.value}"
    ) +
            optional(
                mob.specialization != SpecializationType.NONE,
                "specialization: ${mob.specialization.value}"
            ) +
            optional(
                mob.route.isNotEmpty(),
                "route: ${mob.route.joinToString("-")}"
            ) +
            optional(mob.goldMin > 0, "goldMin: ${mob.goldMin}") +
            optional(mob.goldMax > 0, "goldMax: ${mob.goldMax}")
    return """${int(mob.id)}
${str(mob.name)}
${str(mob.brief)}
${str(mob.description)}
${str(mob.disposition.value)}
${int(mob.hp)}
${int(mob.mana)}
${int(mob.mv)}
${int(mob.level)}
${int(mob.maxItems)}
${int(mob.maxWeight)}
${int(mob.wimpy)}
${int(mob.savingThrows)}
${int(mob.gold)}
${str(mob.race.type.toString().toLowerCase())}
${str(mob.gender.toString())}
${mapSkills(mob.skills)}
${mapAttributes(mob.attributes)}
${mapAffects(mob.affects)}
""" + str(
        (if (optional != "") optional.substring(
            0,
            optional.length - 2
        ) else "")
    ) + "\n"
}
