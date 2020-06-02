package kotlinmud.affect.mapper

import kotlinmud.affect.model.AffectInstance
import kotlinmud.fs.end
import kotlinmud.fs.str

fun mapAffects(affects: List<AffectInstance>): String {
    return affects.joinToString("\n") {
        str("${it.affectType} ${it.timeout}")
    } + end()
}
