package kotlinmud.affect.factory

import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType

fun affect(affectType: AffectType): AffectInstance {
    return AffectInstance(affectType)
}

fun affects(affectType: AffectType): MutableList<AffectInstance> {
    return mutableListOf(AffectInstance(affectType))
}
