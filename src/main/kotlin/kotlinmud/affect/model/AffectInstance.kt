package kotlinmud.affect.model

import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.model.Attributes
import kotlinmud.attributes.type.HasAttributes

data class AffectInstance(
    val affectType: AffectType,
    var timeout: Int = 0,
    override val attributes: Attributes = Attributes()
) : HasAttributes
