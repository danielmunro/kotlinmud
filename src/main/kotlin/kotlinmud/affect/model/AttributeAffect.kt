package kotlinmud.affect.model

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.HasAttributes

class AttributeAffect(affectDAO: AffectDAO) : HasAttributes {
    override val attributes: AttributesDAO = affectDAO.attributes!!
}
