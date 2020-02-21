package kotlinmud.affect

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes

class Affect(val affectType: AffectType, var timeout: Int, override val attributes: Attributes = Attributes()) : HasAttributes