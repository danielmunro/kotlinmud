package kotlinmud.affect.factory

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO

fun createAffect(affectType: AffectType, timeout: Int? = null, attributes: AttributesDAO? = null): Affect {
    return Affect(affectType, timeout, attributes)
}

fun affects(affectType: AffectType): MutableList<Affect> {
    return mutableListOf(createAffect(affectType))
}
