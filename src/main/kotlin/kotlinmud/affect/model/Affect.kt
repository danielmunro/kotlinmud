package kotlinmud.affect.model

import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO

class Affect(val type: AffectType, var timeout: Int? = null, val attributes: AttributesDAO? = null)
