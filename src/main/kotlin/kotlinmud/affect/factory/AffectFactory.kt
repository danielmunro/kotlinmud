package kotlinmud.affect.factory

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO

fun affect(affectType: AffectType, timeout: Int? = null, attributes: AttributesDAO? = null): AffectDAO {
    return AffectDAO.new {
        type = affectType
        this.timeout = timeout
        this.attributes = attributes ?: AttributesDAO.new {}
    }
}

fun affects(affectType: AffectType): MutableList<AffectDAO> {
    return mutableListOf(affect(affectType))
}
