package kotlinmud.affect.model

import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute

class Affect(
    val type: AffectType,
    var timeout: Int? = null,
    val attributes: Map<Attribute, Int>? = null
)
