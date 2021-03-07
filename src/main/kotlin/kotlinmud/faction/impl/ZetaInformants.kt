package kotlinmud.faction.impl

import kotlinmud.empire.impl.Zeta
import kotlinmud.faction.type.Faction
import kotlinmud.faction.type.FactionType

class ZetaInformants : Faction {
    override val name = "Zeta Informants"
    override val type = FactionType.ZETA_INFORMANTS
    override val empire = Zeta()
}
