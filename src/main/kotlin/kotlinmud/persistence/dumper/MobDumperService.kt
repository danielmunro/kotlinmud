package kotlinmud.persistence.dumper

import kotlinmud.mob.model.Mob
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.spec.MobSpec
import kotlinmud.room.model.Area

class MobDumperService(area: Area, private val mobs: List<Mob>) {
    private val mobSpec = MobSpec(area)

    fun dump(): String {
        var buffer = if (mobs.isNotEmpty()) "mobs:\n" else ""
        mobs.forEach { mob ->
            mobSpec.tokens.forEach { token ->
                buffer += when (token.token) {
                    TokenType.ID -> "${mob.id}. "
                    TokenType.Name -> mob.name + "\n"
                    TokenType.Description -> mob.description + "~\n"
                    TokenType.Props -> dumpProps(mob)
                    else -> ""
                }
            }
            buffer += "\n"
        }
        return buffer
    }

    private fun dumpProps(mob: Mob): String {
        return """hp ${mob.hp}~
mana ${mob.mana}~
mv ${mob.mv}~
level ${mob.level}~
race ${mob.race.type.name}~
disposition ${mob.disposition.name}~
job ${mob.job.name}~
specialization ${mob.specialization?.type?.name ?: "None"}~
gender ${mob.gender.name}~
wimpy ${mob.wimpy}~
savingThrows ${mob.savingThrows}~
canonicalId ${mob.canonicalId}~
~
1 1 ${mob.room.id}~
~
"""
    }
}
