package kotlinmud.affect.model

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.attributes.type.HasAttributes

class AttributeAffect(affect: Affect) : HasAttributes {
    override val attributes: Map<Attribute, Int> = affect.attributes!!
}
