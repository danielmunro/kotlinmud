package kotlinmud.affect

import kotlinmud.attributes.HasAttributes
import kotlinmud.attributes.model.Attributes

data class AffectInstance(
    val affectType: AffectType,
    var timeout: Int = 0,
    override val attributes: Attributes = Attributes()
) : HasAttributes
