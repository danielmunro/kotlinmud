package kotlinmud.affect

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes

data class AffectInstance(
    val affectType: AffectType,
    var timeout: Int = 0,
    override val attributes: Attributes = Attributes()
) : HasAttributes
