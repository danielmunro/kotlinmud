package kotlinmud.affect.model

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.HasAttributes

class AttributeAffect(affect: Affect) : HasAttributes {
    override val attributes: AttributesDAO = affect.attributes!!
}
