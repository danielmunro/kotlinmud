package kotlinmud.faction.impl

import kotlinmud.empire.impl.Zeta
import kotlinmud.faction.type.Faction

class ZetaInformants : Faction {
    override val name = "Zeta Informants"
    override val empire = Zeta()
}