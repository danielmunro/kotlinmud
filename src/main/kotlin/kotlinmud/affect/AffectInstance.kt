package kotlinmud.affect

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes

class AffectInstance(
    val affectType: AffectType,
    var timeout: Int,
    override val attributes: Attributes = Attributes()
) : HasAttributes
