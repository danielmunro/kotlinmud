package kotlinmud.persistence.dumper

import kotlinmud.persistence.model.MobModel
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.spec.MobSpec
import kotlinmud.room.model.Area

class MobDumperService(area: Area, private val mobs: List<MobModel>) {
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

    private fun dumpProps(mob: MobModel): String {
        return "${mob.keywords.map { entry -> "${entry.key} ${entry.value}~" }}\n~"
    }
}
