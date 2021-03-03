package kotlinmud.affect.factory

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType

fun createAffect(affectType: AffectType, timeout: Int? = null): Affect {
    return Affect(affectType, timeout)
}

fun affects(affectType: AffectType): MutableList<Affect> {
    return mutableListOf(createAffect(affectType))
}
