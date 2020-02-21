package kotlinmud.affect

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes

class Affect(val affectType: AffectType, override val attributes: Attributes, var timeout: Int) : HasAttributes